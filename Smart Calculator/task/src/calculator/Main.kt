import java.math.BigDecimal
import java.util.*
fun evaluateExpression(expression: String): BigDecimal {
    val operands = Stack<BigDecimal>()
    val operators = Stack<Char>()
    var index = 0

    while (index < expression.length) {
        val token = expression[index]
        when (token) {
            in '0'..'9' -> {
                var num = (token - '0').toBigDecimal()
                while (index + 1 < expression.length && expression[index + 1] in '0'..'9') {
                    num = num * BigDecimal.TEN + ((expression[index + 1] - '0').toBigDecimal())
                    index++
                }
               // println(num)
                operands.push(num)
            }
            '(' -> operators.push(token)
            ')' -> {
                while (operators.peek() != '(') {
                    val op = operators.pop()
                    val op2 = operands.pop()
                    val op1 = operands.pop()
                    operands.push(applyOp(op, op1, op2))
                }
                operators.pop()
            }
            in "+-*/" -> {
                while (operators.isNotEmpty() && hasPrecedence(token, operators.peek())) {
                    val op2 = operands.pop()
                    val op1 = operands.pop()
                    val op = operators.pop()
                    operands.push(applyOp(op, op1, op2))
                }
                operators.push(token)
            }
        }
        index++
    }
    while (operators.isNotEmpty()) {
        val op2 = operands.pop()
        val op1 = operands.pop()
        val op = operators.pop()
        operands.push(applyOp(op, op1, op2))
    }
    return operands.pop()
}

fun hasPrecedence(op1: Char, op2: Char): Boolean {
    if (op2 == '(' || op2 == ')') return false
    if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false
    return true
}

fun applyOp(op: Char, a: BigDecimal, b: BigDecimal): BigDecimal {
    when (op) {
        '+' -> return a + b
        '-' -> return a - b
        '*' -> return a * b
        '/' -> return a / b
    }
    return BigDecimal.ZERO
}

fun isNumeric(toCheck: String): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return toCheck.matches(regex)
}

fun check(str: String): Boolean {
    val regex = "[\\d\\s.+.-]*\\d\$".toRegex()
    return (regex.matches(str))
}

fun checkVar(str: String): Boolean {
    var regex = "[a-z]+".toRegex()
    return (regex.matches(str))
}

class Expression () {

    var varList = mutableMapOf<String, BigDecimal>()

    fun addVar(fstr: String){
        var str = fstr.replace(" ", "")
        var variable = str.substring(0,str.indexOf('='))
        var value = str.substring(str.indexOf('=')+1, str.lastIndex+1)
        if (varList.containsKey(value)) value = varList.get(value).toString()
        if (checkVar(variable) && isNumeric(value)) varList.put(variable,value.toBigDecimal()) else {
            println("Invalid identifier")
            return
        }
        // println(varList)
    }

    fun fromEspression(str: String): BigDecimal{
        var result : BigDecimal = BigDecimal.ZERO
        if (varList.containsKey(str))
            result = varList.get(str)!! else {
            if (isNumeric(str)) result = str.toBigDecimal()
        }
        return result
    }

    fun calc(fstr: String): String{
        var listNum = ""
        if (fstr.contains('=')) addVar(fstr) else
            if (fstr.contains('+') || fstr.contains('-') || fstr.contains('/') || fstr.contains('*')  ){
                try {
                    var numStr = ""
                    for (el in fstr+" ") {
                        if (el.isDigit() || el.isLetter()) {
                            numStr += el.toString()
                        } else {
                            if (numStr != "")  {
                                listNum += fromEspression(numStr).toString()
                            }
                            numStr = ""
                            listNum += el
                    }
                    }
                   // println(final(listNum))

                    println(evaluateExpression(final(listNum)))

                } catch (e: Exception) {
                  //  println(final(listNum))
                    println("Invalid expression")
                }
            } else {
                if (varList.containsKey(fstr.trim())) println(varList.get(fstr.trim())) else println("Unknown variable")
            }

return listNum
    }

    fun final(listNum: String): String {
        var str = listNum.replace(" ", "")
        var newStr = ""
        var count = 0
        var rez = 1
        for (el in str) {
            if (el.isDigit() || el in "*/()") {
                if (count == 0 ) newStr += el else {
                    newStr += if (rez > 0) "+" else "-"
                    newStr += el
                    count = 0
                    rez = 1
                }
            } else {
                if (el in "+-") {
                    when (el) {
                        '+' -> rez = rez * 1
                        '-' -> rez = rez * -1
                    }
                    count++
                    continue
                }

            }
        }
    return newStr
    }
}

fun main() {
    val ex = Expression()
    while (true) {
        val action = readln()
        when {
            action.isEmpty() -> continue
            action == "/exit" -> break
            action == "/help" -> println("The program calculates the sum of numbers")
            else -> {
                if (action.startsWith('/')) {
                    println("Unknown command")
                    continue
                } else {
                    ex.calc(action)
                }

            }
        }
    }
    println("Bye!")
}
