package com.example.kalkulatorsederhanakotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var operasiTambah: Boolean = false
    private var bilanganDesimal: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun aksiAngka(view: View) {

        if (view is Button) {

            if (view.text == ".") {

                if (bilanganDesimal) {

                    workingsTV.append(view.text)

                }

                bilanganDesimal = false;

            } else {

                workingsTV.append(view.text)

            }

            operasiTambah = true

        }

    }


    fun aksiOperator(view: View) {

        if (view is Button && operasiTambah) {

            workingsTV.append(view.text)

            operasiTambah = false

            bilanganDesimal = true
        }

    }

    fun aksiHapusSemua(view: View) {

        workingsTV.text = ""
        resultsTV.text = ""

    }

    fun aksiHapusSekali(view: View) {

        val length = workingsTV.length()
        if (length > 0) {
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
        }

    }

    fun aksiHitung(view: View) {

        resultsTV.text = hitungHasil()

    }

    private fun hitungHasil(): String {

        val digitOperator = digitOperator()
        if (digitOperator.isEmpty()) {
            return ""
        }

        val timesDivision = hitungPerkalian(digitOperator)
        if (timesDivision.isEmpty()) {
            return ""
        }

        val result = hitungTambahKurang(timesDivision)
        return result.toString()


    }

    private fun hitungTambahKurang(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '-') {
                    result -= nextDigit
                }
            }
        }

        return result
    }

    private fun hitungPerkalian(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = hitungKali(list)
        }
        return list
    }

    private fun hitungKali(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float

                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex) {
                newList.add(passedList[i])
            }
        }
        return newList
    }

    private fun digitOperator(): MutableList<Any> {

        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingsTV.text) {
            if (character.isDigit() || character == '.') {
                currentDigit += character
            } else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "") {
            list.add(currentDigit.toFloat())
        }
        return list

    }

}