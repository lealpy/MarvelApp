package com.lealpy.marvelapp.presentation.screens.characters

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lealpy.marvelapp.domain.models.Character
import com.lealpy.marvelapp.domain.models.SortBy
import com.lealpy.marvelapp.domain.use_cases.GetCharactersUseCase
import com.lealpy.marvelapp.presentation.utils.Const.APP_LOG_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
) : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    private val _progressBarVisibility = MutableLiveData<Int>()
    val progressBarVisibility: LiveData<Int> = _progressBarVisibility

    private val disposable = CompositeDisposable()

    init {
        getCharacters()
    }

    fun onSwipedRefresh() {
        getCharacters()
    }

    fun onSortByClicked(sortBy: SortBy) {
        val characters = _characters.value
        if (characters != null) {
            when (sortBy) {
                SortBy.BY_ALPHABET -> {
                    _characters.value = characters.sortedBy { character ->
                        character.name
                    }
                }
                SortBy.BY_DATE -> {
                    _characters.value = characters.sortedBy { character ->
                        character.modified
                    }
                }
                SortBy.BY_ALPHABET_DESCENDING -> {
                    _characters.value = characters.sortedByDescending { character ->
                        character.name
                    }
                }
                SortBy.BY_DATE_DESCENDING -> {
                    _characters.value = characters.sortedByDescending { character ->
                        character.modified
                    }
                }
            }
        } else {
            getCharacters()
        }
    }

    private fun getCharacters() {
        _progressBarVisibility.value = View.VISIBLE

        disposable.add(
            getCharactersUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { characters ->
                        _characters.value = characters
                        _progressBarVisibility.value = View.GONE
                    },
                    { error ->
                        Log.e(APP_LOG_TAG, error.message.toString())
                        _progressBarVisibility.value = View.GONE
                    }
                )
        )
    }

}