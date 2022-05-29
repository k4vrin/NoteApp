package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.cleanarchitecturenoteapp.ui.theme.*

@Entity
data class Note(
	val title: String,
	val content: String,
	// To order notes by date
	val timestamp: Long,
	val color: Int,
	@PrimaryKey
	val id: Int? = null
) {
	companion object {
		val noteColors = listOf(
			RedOrange,
			LightGreen,
			Violet,
			BabyBlue,
			RedPink
		)
	}
}

/**
 * Invalid note exception
 *
 * @constructor
 *
 * @param message
 *
 * Validating the note that we want to insert
 */
class InvalidNoteException(message: String) : Exception()
