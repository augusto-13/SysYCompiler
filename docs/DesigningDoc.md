# 设计文档 [20373493-李逸卓]

## 1. FrontEnd<前端>

### 概述：

前端共分为四大部分：

+ 词法分析(`lexer`)
+ 语法分析(`parser`)
+ 静态错误检查(`errorChecker`)
+ 中间代码生成(`IRGenerator`)

代码文件结构如下：

![image-20221225195806476](C:\Users\86139\AppData\Roaming\Typora\typora-user-images\image-20221225195806476.png)

其中，`nodes`是在语法分析阶段建立起的语法树的节点，后面的错误处理与中间代码生成均是通过对语法树的遍历（对每个节点调用一个泛化的方法[`checkError()`/`genIR()`]）来实现。

各包内子结构将在下方逐个介绍，窃以为除了一个编译器内建了三遍的符号表ShitMountain以外，总体架构还算精巧。

`Compiler.java`中前端的运行逻辑如下：

```
if (level < 1 || level > 5) {
            System.out.println("Illegal level value");
            return;
        }
        // level 1: FrontEnd.lexer
        File in = new File("testfile.txt");
        File out = new File("output.txt");
        init(out);
        FileSource fileSrc = new FileSource(in);
        Lexer lexer = new Lexer(fileSrc);
        ArrayList<Token> tokens = lexer.tokenize();
        if (lexer.hasError()) {
            lexer.printError();
            return;
        }
        if (level == 1) return;

        // level 2: FrontEnd.parser
        TokenSource tokenSrc = new TokenSource(tokens);
        Parser parser = new Parser(tokenSrc);
        root = parser.parse();
        root.printTo(out);
        if (level == 2) return;

        // level 3: FrontEnd.errorHandler
        File err = new File("error.txt");
        init(err);
        ErrorHandler errorHandler = new ErrorHandler(root, err);
        if (errorHandler.hasError()) {
            System.out.println("There's something wrong with your code.\nPlease check \"error.txt\".\nStop at level 3.");
            noError = false;
            return;
        }
        if (level == 3) return;

        // level 4: irGeneration
        File ir = new File("20373493_李逸卓_优化前中间代码.txt");
        init(ir);
        IRGenerator irGenerator = new IRGenerator(root);
        irGenerator.printTo(ir);
        if (level == 4) return;

        // level 5: irOptimization
        File ir_ = new File("20373493_李逸卓_优化后中间代码.txt");
        init(ir_);
        IROptimizer irOptimizer = new IROptimizer();
        irOptimizer.printTo(ir_);
```

### A. Lexer

使用`Lexer`类完成，内部设计较为复杂，当时参考了一位学长的设计，有种“为了面向对象而面向对象的嫌疑”。

### B. Parser

表达式是左递归文法，这里需要对其进行转化。但转化后的语法会让同一级表达式从右往左计算，`1-1-1`变成了 `1-(1-1)=0`。这时，需要能将某个节点插在当前节点上方。这里的操作比较神奇。在进入二元运算一开始，就先记录 `children` 的 `size`。在进入函数时，不记录当前 `children` 栈的大小，而是前面说的，其父节点函数记录的 `children` 的 `size`。这样，在这个节点 `finishNode` 时，自然而然就将比右运算早入栈的左运算划为了其子节点。

### C. Static Type-checking (Error Handling)

这部分的重点在于符号表设计与上下文信息的传递。

上下文信息使用`Context.java`进行装载，其内部所有属性均为`public`类，各节点均可见。

### D. Intermediate Representation Generation

我的`IR`设计分为以下`16`类：

![image-20221225233204464](C:\Users\86139\AppData\Roaming\Typora\typora-user-images\image-20221225233204464.png)

## 2. BackEnd
### 2-1. 为中间代码填坑
#### 2-1-1. 全局声明
+ 在设计全局变量声明中间代码时，将声明过程分为“定义”与“初始化”两部分，其中“初始化”由`assign`语句实现，这个过程很不合理，原因有二：
  + 全局本身的声明与初始化本身便是两个密不可分的过程：开头在`.data`字段声明，调用时使用`lw`, 赋值时使用`sw`。
  + 在开始时想要将二者分离实现是因为`InitVal`中可能会出现非常量，但是由于在全局初始化阶段没有任何的重新赋值操作，因此即使非常量的值也是确定的，需要特殊处理。
+ 常量数组也需要存储
  + 对于形如下方代码块中的操作，`offset`为变量的情况仍然需要从内存中提取变量。
    ```
    const int a[2] = {0, 1};
    int b;
    b = a[b];
    ```
#### 2-1-2. 中间码存储
+ `data`段与`text`段需要分开处理，因此需要将全局常/变量与打印字符串声明单独存储
+ `text`段内部函数声明与主体也需要单独存储
  + 需要在`IRContext`中增加上下文`in_func_def`信息，帮助区分哪些`Stmt`在函数内部。

#### 2-1-3. `_3_Assign_Q`

+ 我们希望只有在对非临时变量进行赋值时使用该类语句，即左值不会出现**临时变量**
+ 对于**临时变量**倒寄存器这类操作，在`Exp`内部实现

#### 2-1-4. 各类变量命名

+ 全局：`@`
+ main：`%`
+ para：`*`
+ func_var：`!`

### 2-2. 机器码生成

#### 2-2-1. `data`字段
+ `const array`
+ `var`
+ `var array`
+ `const str`

分以下四类翻译即可。

#### 2-2-2. `MIPS`布局

#### 2-2-3. 寄存器使用

| Name            | Number | 保留 | 原因                                                         |
| --------------- | ------ | ---- | ------------------------------------------------------------ |
| $zero           | 0      | Y    |                                                              |
| $at             | 1      | Y    | 当`reg-reg-imm`指令的立即数出现越界时，Mars会将其当做`伪指令`识别，并使用`$1`寄存器与`ori`,`lui`指令将其转化成三条指令，不可占用 |
| $v0             | 2      | Y    | 1. 存储函数返回值<br/>2. 中断调用时会使用                    |
| $a0             | 4      | Y    | 中断调用时使用                                               |
| $sp(0x7fffeffc) | 29     | Y    | 栈指针，函数调用与声明内部操作均会使用                       |
| $fp             | 30     | Y    | 存储堆的初始地址(0x10040000)，好像没什么大用                 |
| $ra             | 31     | Y    | 函数调用时跳转指令保存返回指令位置                           |


```
int[] tmp_regs = {3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 24, 25, 26, 27, 28};
int[] globalregs = {16, 17, 18, 19, 20, 21, 22, 23};
private HashMap<Integer, Boolean> regpool = new HashMap<>();
private HashMap<Integer, Sym> globalreg2sym = new HashMap<>(); //16-23
private HashMap<Integer, Sym> tmpreg2sym = new HashMap<>(); //3 5-15 24-28
```

#### 2-2-4. 注意事项

目标码生成顺序：

1. 全局变量
   + 建立变量-内存符号表
2. 函数声明
   + 为函数打印跳转标签，我们令**函数标签**就是本身**标识符**，方便。
   + 建立临时的函数内部变量-内存映射表，随时清除
   + 查表也很有技巧：
     + 先查局部变量
     + 再查参数
     + 最后查全局变量
3. 主函数
   + 最后一步

#### 2-2-5. 各中间代码的具体转换

#### A. `_1_VarDecl_Q` / `_2_ArrDecl_Q`

##### 主函数中

+ 对于`main`函数，局部变量存储在堆段，由于本课程没有涉及动态空间分配和跨文件调用，所以将堆的地址(0x10040000)作为一个常量在`java`代码中使用，使用时就存入符号表并累加，避免了使用指针来进行存取。
+ 我在实现非全局变量声明时，将**初始化**移入赋值语句中实现，因此局部变量声明没有代码生成，只维护一个符号表。

#### B. `_12_Label_Q`

+ 难点：为函数加标签

#### C. `_3_Assign_Q`

+ 事先明确，需要处理的左值包括：
  + 已声明的各类**数组元素**与**变量**的赋值
  + 未声明的临时变量的赋值



对于一般的函数，局部变量存储在**栈**中，即`$sp`指针指向的地址中。

普通函数的局部变量存储时设计成采用相对于堆的负数的地址，比如`sw $2 -12($sp)`，这样做是为了和实参数进行区分，函数由于要接受传入的实参，因此实参必定是在`$sp`之上，为了不引起递归时的混淆，参数统一在函数内部运行时在`$sp`之上，局部变量则在`$sp`之下。

#### 2-2-6.  函数

目标代码生成最难的一部分，有以下难点，逐个击破：

##### A. 函数调用时，压参时，对`$29($sp)`的操作[-]

##### B. 函数声明中，递归调用压参的处理[-]

二者同理，调用前后：

+ `sw $31, $29(sp_offset)`
+ 压实参（-4 * k）
+ `$sp <-- $sp - 4 * (k + 1)`
+ `jal`
+ `$sp <-- $sp + 4 * (k + 1)`
+ `lw $31, $29(sp_offset)`

##### C. 函数声明中，符号表查找并提取参数时的操作[+]

+ 明确：

  1. 这是只有声明中会出现的行为

  2. `$sp`上方的行为

     + 假设一共有`k`个参数

     + ```
       // 不会用到$ra
       // $ra ---> +(4k+4)
       // 就是找个位置存一下
       para_0 ---> +(4k)
       para_1 ---> +(4k-4)
       .
       .
       .
       para_k-2 ---> +(8)
       para_k-1 ---> +(4)
       <(k - i) * 4>
       ```

##### D. 函数声明中，变量声明的处理[-]

+ 从`0`开始
+ 每次减4
+ 如果存储长度为`k`的数组：
  + `-4*(k-1)`
  + 存储
  + `-4`

### 2-3. 指令的装载

```
public abstract class MIPSCode {

    public static class LI extends MIPSCode {...}

    public static class Label extends MIPSCode {...}

    public static class Enter extends MIPSCode {...}

    public static class LA extends MIPSCode {...}

    public static class SW extends MIPSCode {...}

    public static class LW extends MIPSCode {...}

    public static class Cal_RR extends MIPSCode {...}

    public static class Cal_RI extends MIPSCode {...}

    public static class Move extends MIPSCode {...}

    public static class J extends MIPSCode {...}

    public static class BEZ extends MIPSCode {...}

    public static class BNZ extends MIPSCode {...}

    public static class JAL extends MIPSCode {...}

    public static class JR extends MIPSCode {...}


}
```

使用了这样的一个巧妙的**内部静态类**的形式来进行指令装载。

## 结尾



> 编译第一次实验课PPT上往届学长的感想分享中的一句话让人记忆犹新：“想，都是问题，做，才是答案。”
>
> 1. 有一些问题，从纯理论的角度理解起来往往是困难的；还有些时候，你以为自己理解了某个概念的本质，但实际上你仅仅只是认识了皮毛。
>    举一个最经典的例子：语法制导翻译SDT。
>    SDT从文法的角度来分析，其实就是在普通文法序列中插入“动作符号”形成动作序列，如果单单从书中给的例子上去理解，这个概念是比较简单的、并且容易理解的；但是从这里直接过渡到手搓一个编译器中端，在实际操作时，至少我是一头雾水，因为从文法改写角度理解的SDT虽然直观却难以操作。
>    而在实际操作中，我们往往会在Parser阶段构建一棵语法树，对节点进行遍历操作，在遍历的过程中来进行一系列的操作与计算。写中间代码生成的时候大概明白自己在做什么，在即将写完的时候我才理解了“语法树”的操作模式与SDT之间的联系：
>    语法树中父节点与各子节点间是推导式左侧与右侧的关系；兄弟节点刚好组成一个推导式右部的符号序列。而我们在遍历语法树的过程中(e.g. A → aBc)，对A节点调用genIR()【中间代码生成，一个method】的内容是，分别对a, B, c三个子节点递归调用同一函数，并在三次调用之间进行某些操作，从而在某个上下文语境中实现我们的目的。而这和“文法改写”SDT的本质是一致的，在某特定语境下进行特定的操作达成目的。
> 2. 有一些困难，凭空去想往往只会越想越难。动手去写了，困难才有了迎刃而解的可能。
>    我的个人经历很可以说明问题了，代码生成一作业因为迟迟想不出一个合适的架构，迟迟没有动笔，再加上当时自己比较忙，所以错过了ddl没有交上。代码生成二也是如此，因为迟迟怯于动笔，硬是把完成时间拖到了DDL当天。这段时间的经历告诉我：“写中间代码的时候思考后端可能遇到的困难，写后端时想优化可能遇到的问题”是一个非常不健康的习惯。专注于眼前的挑战，一点点解决问题、战胜困难而不是被未来的困难吓住是一个很值得培养的品质。
>
> 时间限制就写这些吧，去de竞速排序的bug了（悲