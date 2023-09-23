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
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // БЛОК ОБЪЯВЛЕНИЯ И ИНИЦИАЛИЗАЦИИ
        val backButton : ImageButton = findViewById(R.id.button_back)
        val searchField : EditText = findViewById(R.id.search_field)
        val clearButton : ImageButton = findViewById(R.id.button_clear)

        // ВЫЗВАТЬ МЕТОД ONDESTROY ТЕКУЩЕЙ АКТИВИТИ
        backButton.setOnClickListener { finish() }

        clearButton.setOnClickListener {
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
                clearButton.visibility = clearButtonVisibility(string)
            }
        }
        searchField.addTextChangedListener(textWatcher)
    }

    // МЕТОД ДЛЯ РАБОТЫ С ПАРАМЕТРОМ VISIVILITY ЛЮБОГО НАСЛЕДНИКА VIEW
    fun clearButtonVisibility (string : CharSequence?) : Int
    {
        return if (string.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}