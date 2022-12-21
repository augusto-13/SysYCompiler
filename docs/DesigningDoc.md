# 设计文档
**加粗**部分为当前思考进度
## FrontEnd
### A. Lexer
### B. Parser
### C. Static Type-checking (Error Handling)
### D. Intermediate Representation Generation
#### 1. `FrontEnd`内部结构重新梳理
+ `<package> lexer` `(词法分析器)`
+ `<package> parser` `(语法分析器)`
+ `<package> errorChecker` `(错误处理器[静态类型检查])`
+ `<package> nodes` `(抽象语法树[AST]节点)`
+ `<package> IRGenerator` `(中间表示生成器)`
  + `<package> IRTbl` `(中间结构符号表[主要用于常量的处理])`
  + `<package> Quadruple` `(四元式结构)`
  + `<class> IRCodes` `(四元式列表)`
  + `<class> IRGenerator` `(中间表示生成器，起与"后端_Backend"、"抽象语法树节点_nodes"以及"中间结构优化器[IROptimizer]"交互的作用)`
  + `<class> IROptimizer` **(Not implemented yet)**
#### 2. 总体执行流
+ 
## 2. BackEnd
### 2-1. 为中间代码填坑
#### 2-1-1. 全局变量声明
+ 在设计全局变量声明中间代码时，将声明过程分为“定义”与“初始化”两部分，其中“初始化”由`assign`语句实现，这个过程很不合理，原因有二：
  + 全局本身的声明与初始化本身便是两个密不可分的过程：开头在`.data`字段声明，调用时使用`lw`, 赋值时使用`sw`。
  + 在开始时想要将二者分离实现是因为`InitVal`中可能会出现非常量，但是由于在全局初始化阶段没有任何的重新赋值操作，因此即使非常量的值也是确定的，需要特殊处理。
+ 