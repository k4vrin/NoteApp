package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
	private val repository: NoteRepository
) {

	// To validate the notes
	@Throws(InvalidNoteException::class)
	suspend operator fun invoke(note: Note) {
		if (note.title.isBlank()) {
			throw InvalidNoteException(message = "The title of the note can't be empty.")
		}
		if (note.content.isBlank()) {
			throw InvalidNoteException(message = "The content of the note can't be empty.")
		}
		repository.insertNote(note = note)
	}
}