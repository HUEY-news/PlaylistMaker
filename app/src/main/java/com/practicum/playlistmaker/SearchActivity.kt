package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity()
{
    // БЛОК КЛЮЧЕВЫХ ЗНАЧЕНИЙ
    companion object
    {
        // КЛЮЧ ДЛЯ ХРАНЕНИЯ СОДЕРЖИМОГО ПОИСКОВОЙ СТРОКИ
        const val SEARCH_FIELD_CONTENT = "SEARCH_FIELD_TEXT"
    }

    // ГЛОБАЛЬНАЯ ПЕРЕМННАЯ ДЛЯ ХРАНЕНИЯ СОДЕРЖИМОГО ПОИСКОВОЙ СТРОКИ
    lateinit var searchFieldContent : String

    override fun onCreate(state: Bundle?)
    {
        super.onCreate(state)
        setContentView(R.layout.activity_search)

        // ВЫЗВАТЬ МЕТОД ONDESTROY ТЕКУЩЕЙ АКТИВИТИ
        val backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener { finish() }

        val searchField = findViewById<EditText>(R.id.search_field)
        val resetButton = findViewById<ImageButton>(R.id.button_reset)
        resetButton.setOnClickListener {
            // ОЧИСТИТЬ ПОЛЕ ВВОДА
            searchField.setText("")
            // ВСПОМОГАТЕЛЬНЫЙ КЛАСС. ПОСРЕДНИК МЕЖДУ ИСТОЧНИКОМ ВВОДА И ПРИЛОЖЕНИЕМ
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            // СКРЫТЬ КЛАВИАТУРУ
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)}

        // ИНТЕРФЕЙС СЛУШАТЕЛЯ ДЛЯ EDITTEXT
        val textWatcher = object : TextWatcher
        {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int)
            {
                // МЕНЯЕМ ВИДИМОСТЬ КНОПКИ "CLEAR"
                resetButton.visibility = clearButtonVisibility(string)

                //СОХРАНЯМ ВВОД В ГЛОБАЛЬНУЮ ПЕРЕМЕННУЮ
                if (string != null) searchFieldContent = string.toString()
            }
        }
        searchField.addTextChangedListener(textWatcher)
    }

    // МЕТОД ДЛЯ РАБОТЫ С ПАРАМЕТРОМ VISIVILITY ЛЮБОГО НАСЛЕДНИКА VIEW
    fun clearButtonVisibility (string : CharSequence?) : Int
    {
        return if (string.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun onRestoreInstanceState(state: Bundle)
    {
        super.onRestoreInstanceState(state)
        // ЗАГРУЖАЕМ СОДЕРЖИМОЕ ПОЛЯ ПОИСКА
        if (state != null) searchFieldContent = state.getString(SEARCH_FIELD_CONTENT, "")
    }

    override fun onSaveInstanceState(state: Bundle)
    {
        super.onSaveInstanceState(state)
        // СОХРАНЯЕМ СОДЕРЖИМОЕ ПОЛЯ ПОИСКА
        state.putString(SEARCH_FIELD_CONTENT, searchFieldContent)
    }
}