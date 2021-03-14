package com.example.calcapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity(), View.OnClickListener{
    private val numArray = mutableListOf<String>("") //数値格納用配列
    private val symbolArray = mutableListOf<String>("") //記号格納用配列
    private var zeroDivisionFlag = false //フラグ：true->ゼロ除算を行っている
    private var zeroFlag = false //フラグ：true->数値の先頭がゼロ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        answerText.text = "0"
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        ac_button.setOnClickListener(this)
        dot_button.setOnClickListener(this)
        plus_button.setOnClickListener(this)
        minus_button.setOnClickListener(this)
        multi_button.setOnClickListener(this)
        division_button.setOnClickListener(this)
        equal_button.setOnClickListener(this)
    }

    /* 配列に入力した数値を格納する関数 */
    private fun addArray(str: String, index: Int, arr: MutableList<String>):String{
        if(index == arr.lastIndex) {
            return str.substring(arr[index].length)
        }
        return addArray(str.substring(arr[index].length + 1), index + 1, arr)
    }

    /* 配列をクリアする関数 */
    private fun clearArray(numArray: MutableList<String>, symbolArray: MutableList<String>){
        numArray.clear()
        symbolArray.clear()
        numArray.add("")
        symbolArray.add("")
    }

    /* 配列に格納された値を用いて計算（乗除）する関数 */
    private fun calcArray1(numArray: MutableList<String>, symbolArray: MutableList<String>, index: Int):MutableList<String>{
        val copyArray:MutableList<String> = mutableListOf() //数値格納用配列

        if(index > symbolArray.lastIndex) return calcArray2(numArray, symbolArray, 1)
        /* 加減の式はそのままコピー */
        else if(symbolArray[index] == "+" || symbolArray[index] == "-"){
            numArray.forEach{
                copyArray.add(it)
            }
            return calcArray1(copyArray, symbolArray, index + 1)
        }
        else{
            /* 乗除の式の手前までコピー */
            for(i in 0..index){
                if(index == i) break
                copyArray.add(numArray[i])
            }
            val num1:BigDecimal = BigDecimal(numArray[index])
            val num2:BigDecimal = BigDecimal(numArray[index + 1])
            /* 乗除の計算 */
            when(symbolArray[index]){
                "÷" -> {
                    if(num2.compareTo(BigDecimal.ZERO) == 0){
                        zeroDivisionFlag = true
                        return numArray
                    }
                    else {
                        copyArray.add(num1.divide(num2, 7, RoundingMode.HALF_UP).toString())
                    }
                }
                "×" -> copyArray.add(num1.multiply(num2).toString() )
            }
            /* 残りをそのままコピー */
            for(i in index + 2..numArray.lastIndex){
                if(i > numArray.lastIndex) break
                copyArray.add(numArray[i])
            }
            symbolArray.removeAt(index) //計算に使用した÷や×は削除
            return calcArray1(copyArray, symbolArray, index)
        }
    }

    /* 配列に格納された値を用いて計算（加減）する関数 */
    private fun calcArray2(numArray: MutableList<String>, symbolArray: MutableList<String>, index: Int):MutableList<String>{
        val copyArray:MutableList<String> = mutableListOf() //数値格納用配列

        if(index > symbolArray.lastIndex) return numArray
        else {
            /* 加減の式の手前までコピー */
            for(i in 0..index){
                if(index == i) break
                copyArray.add(numArray[i])
            }
            /* 加減の計算 */
            val num1:BigDecimal = BigDecimal(numArray[index])
            val num2:BigDecimal = BigDecimal(numArray[index + 1])
            when(symbolArray[index]){
                "+" -> copyArray.add(num1.plus(num2).toString() )
                "-" -> copyArray.add(num1.minus(num2).toString() )
            }
            /* 残りをそのままコピー */
            for(i in index + 2..numArray.lastIndex){
                if(i > numArray.lastIndex) break
                copyArray.add(numArray[i])
            }
            symbolArray.removeAt(index) //計算に使用した+や-を削除
            return calcArray2(copyArray, symbolArray, index)
        }
    }

    /* 入力の最後が数値か判別する関数 */
    private fun endCheck(str: String):Boolean{
        for(i in 0..9){
            if(str.endsWith(i.toString())) return true
        }
        return false
    }

    /* 入力された文字列に+,-,÷,×のいずれかが含まれているか調べる関数 */
    private fun symbolCheck(str: String):Boolean{
        val symbol = arrayOf("+", "-", "×", "÷")
        for(i in symbol){
            if(str.indexOf(i) >= 0) return true
        }
        return false
    }

    /* ボタンクリック時の処理を行う関数 */
    override fun onClick(view: View){
        if((zeroDivisionFlag||answerText.length() == 25) && (R.id.ac_button != view.id&&R.id.equal_button != view.id)){ //エラー時の処理
            return
        }
        if(zeroFlag){//先頭がゼロのときの入力制限
            zeroFlag = when(view.id){
                R.id.dot_button -> false
                R.id.ac_button -> false
                R.id.equal_button -> false
                R.id.plus_button -> false
                R.id.minus_button -> false
                R.id.division_button -> false
                R.id.multi_button -> false
                else -> return
            }
        }
        if(numArray[0] == answerText.text.toString()){ //計算結果出力後の処理
            when(view.id){
                R.id.button0 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button1 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button2 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button3 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button4 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button5 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button6 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button7 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button8 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.button9 -> {
                    answerText.text=""
                    clearArray(numArray,symbolArray)}
                R.id.dot_button -> {
                    answerText.text="0"
                    clearArray(numArray,symbolArray)}
                else -> clearArray(numArray,symbolArray)
            }
        }
        if(R.id.button0 == view.id){ //0のボタンの処理
            if(addArray(answerText.text.toString(),0,numArray) != "0"){
                val checkStr = answerText.text.toString().takeLast(1)
                if(checkStr == "+" || checkStr == "-" || checkStr == "×" || checkStr == "÷"){
                    zeroFlag = true
                }
                answerText.text = answerText.text.toString() +"0"
            }
        }
        if(R.id.button1 == view.id){ //1のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "1"
            }
            else{
                answerText.text = answerText.text.toString() + "1"
            }
        }
        if(R.id.button2 == view.id){ //2のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "2"
            }
            else{
                answerText.text = answerText.text.toString() + "2"
            }
        }
        if(R.id.button3 == view.id){ //3のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "3"
            }
            else{
                answerText.text = answerText.text.toString() + "3"
            }
        }
        if(R.id.button4 == view.id){ //4のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "4"
            }
            else{
                answerText.text = answerText.text.toString() + "4"
            }
        }
        if(R.id.button5 == view.id){ //5のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "5"
            }
            else{
                answerText.text = answerText.text.toString() + "5"
            }
        }
        if(R.id.button6 == view.id){ //6のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "6"
            }
            else{
                answerText.text = answerText.text.toString() + "6"
            }
        }
        if(R.id.button7 == view.id){ //7のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "7"
            }
            else{
                answerText.text = answerText.text.toString() + "7"
            }
        }
        if(R.id.button8 == view.id){ //8のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "8"
            }
            else{
                answerText.text = answerText.text.toString() + "8"
            }
        }
        if(R.id.button9 == view.id){ //9のボタンの処理
            if(answerText.text == "0"){
                answerText.text = "9"
            }
            else{
                answerText.text = answerText.text.toString() + "9"
            }
        }
        if(R.id.dot_button == view.id){ //.のボタンの処理
            if(endCheck(answerText.text.toString()) && addArray(answerText.text.toString(), 0, numArray).indexOf(".") < 0){
                answerText.text = answerText.text.toString() + "."
            }
        }
        if(R.id.ac_button == view.id){ //ACのボタンの処理
            clearArray(numArray, symbolArray)
            zeroDivisionFlag = false
            answerText.text = "0"
        }
        if(R.id.plus_button == view.id){ //+のボタンの処理
            if(endCheck(answerText.text.toString())){
                numArray.add(addArray(answerText.text.toString(), 0, numArray))
                symbolArray.add("+")
                answerText.text = answerText.text.toString() + "+"
            }
        }
        if(R.id.minus_button == view.id){ //-のボタンの処理
            if(endCheck(answerText.text.toString())){
                numArray.add(addArray(answerText.text.toString(), 0, numArray))
                symbolArray.add("-")
                answerText.text = answerText.text.toString() + "-"
            }
        }
        if(R.id.multi_button == view.id){ //×のボタンの処理
            if(endCheck(answerText.text.toString())){
                numArray.add(addArray(answerText.text.toString(), 0, numArray))
                symbolArray.add("×")
                answerText.text = answerText.text.toString() + "×"
            }
        }
        if(R.id.division_button == view.id){ //÷のボタンの処理
            if(endCheck(answerText.text.toString())){
                numArray.add(addArray(answerText.text.toString(), 0, numArray))
                symbolArray.add("÷")
                answerText.text = answerText.text.toString() + "÷"
            }
        }
        if(R.id.equal_button == view.id){ //=のボタンの処理
            if(endCheck(answerText.text.toString()) && symbolCheck(answerText.text.toString())) {
                numArray.add(addArray(answerText.text.toString(), 0, numArray))
                answerText.text = ""
                var ans = calcArray1(numArray, symbolArray, 1).last()
                if(zeroDivisionFlag){
                    ans = "ゼロ除算はできません"
                }
                else if(BigDecimal(ans).compareTo(BigDecimal.ZERO) == 0){
                    ans ="0"
                }
                answerText.text = ans
                clearArray(numArray, symbolArray)
                numArray.add(answerText.text.toString())
                numArray.removeAt(0)
            }
        }
    }
}