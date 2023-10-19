import kotlin.math.absoluteValue

fun main(){
    val arr = ArrayList<Pair>()
    val logger = Logger()
    arr.add(CommonFraction(45, 75))
    arr.add(ComplexNumeral(3, 7))
    arr.add(Pair(1, 1))
    for (p in arr){
        PairLogger(logger).log(p.getInfo())
    }
    val arrOp = ArrayList<PairJ<CommonFraction, CommonFraction>>()
    arrOp.add(PairJ(CommonFraction(3, 4), CommonFraction(5, 6)))
    arrOp.add(PairJ(CommonFraction(18, 7), CommonFraction(11, 23)))
    arrOp.add(PairJ(CommonFraction(1, 5), CommonFraction(900, 15)))
    arrOp.add(PairJ(CommonFraction(1, 5), CommonFraction(1, 5)))
    arrOp.add(PairJ(CommonFraction(-1, 5), CommonFraction(2, 5)))
    arrOp.add(PairJ(CommonFraction(1, -5), CommonFraction(2, 5)))
    for (ops in arrOp){
        if(ops.op1.isValidate() && ops.op2.isValidate()) {
            print("${ops.op1.getInfo()} + ${ops.op2.getInfo()} = ${ops.op1.plus(ops.op2).getInfo()}\n")
            print("${ops.op1.getInfo()} * ${ops.op2.getInfo()} = ${ops.op1.mul(ops.op2).getInfo()}\n")
            print("${ops.op1.getInfo()} - ${ops.op2.getInfo()} = ${ops.op1.sub(ops.op2).getInfo()}\n")
            print("${ops.op1.getInfo()} / ${ops.op2.getInfo()} = ${ops.op1.div(ops.op2).getInfo()}\n")
        }else{
            if(!ops.op1.isValidate()){
                println("Z is in the ${ops.op1.getInfo()} == 0!")
            }else if(!ops.op2.isValidate()){
                println("Z is in the ${ops.op2.getInfo()} == 0!")
            }
        }
    }
}

class PairJ<K, V>(var op1: K, var op2: V)

interface IPrintable{
    fun getInfo(): String
}

open class Pair(protected open var x: Int, protected open var y: Int): IPrintable{

    override fun getInfo(): String {
        return "$x, $y"
    }
}

class ComplexNumeral(override var x: Int, override var y: Int): Pair(x, y){
    override fun getInfo(): String {
        return "($x,$y)"
    }
}

class CommonFraction(x: Int, y: Int) : Pair(x, y) {

    init {
        this.y = if (x == 0) {
            0
        }else{
            y
        }
        this.x = if (y < 0) {
            -x
        }else{
            x
        }
        this.y = if (y < 0) {
            -y
        }else{
            y
        }
    }

    override fun getInfo(): String {
        return if (x == 0){
            "0"
        } else if (y == 0){
            "$x"
        } else if (x == y){
            "$x"
        } else if (x < 0 || y < 0){
            return if(x.absoluteValue % y.absoluteValue == 0) {
                "-${x.absoluteValue/y.absoluteValue}"
            } else if(x.absoluteValue > y.absoluteValue){
                "-${x.absoluteValue/y.absoluteValue} ${x.absoluteValue%y.absoluteValue}/${y.absoluteValue}"
            }else{
                "-${x.absoluteValue}/${y.absoluteValue}"
            }
        } else if (x > y){
            if(x % y == 0){
                "${x/y}"
            }else {
                "${x/y} ${x%y}/$y"
            }
        } else{
            "$x/$y"
        }
    }

    private fun gcd(z1: Int, z2: Int): Int{
        return if (z2 == 0)
            z1
        else
            gcd(z2, z1 % z2)
    }

    fun isValidate(): Boolean{
        return y != 0
    }

    fun plus(op2: CommonFraction): CommonFraction{
        var z1 = y
        var z2 = op2.y
        var z_r = z1
        var ch_r = x + op2.x
        if(z1 != z2){
            ch_r = x * z2 + op2.x * z1
            z_r = z1 * z2
        }
        var gcd = gcd(ch_r, z_r)
        ch_r /= gcd
        z_r /= gcd
        return CommonFraction(ch_r, z_r)
    }

    fun mul(op2: CommonFraction): CommonFraction{
        var z_r = y * op2.y
        var ch_r = x * op2.x
        var gcd = gcd(ch_r, z_r)
        ch_r /= gcd
        z_r /= gcd
        return CommonFraction(ch_r, z_r)
    }

    fun sub(op2: CommonFraction): CommonFraction{
        var z_r = y * op2.y
        var ch_1 = x * op2.y
        var ch_2 = op2.x * y
        var ch_r = ch_1 - ch_2
        var gcd = gcd(ch_r, z_r)
        ch_r /= gcd
        z_r /= gcd
        return CommonFraction(ch_r, z_r)
    }

    fun div(op2: CommonFraction): CommonFraction{
        return this.mul(CommonFraction(op2.y, op2.x))
    }
}

interface ILogger{
    fun log(message: String)
}

class Logger: ILogger {

    override fun log(message: String) {
        println("Logger:\n$message")
    }
}

class PairLogger(l: Logger): ILogger by l

