package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.use_case

/**
 * Note use cases
 *
 * @property getNotesUseCase
 * @property deleteNoteUseCase
 * @constructor Create empty Note use cases
 *
 * This is a class tha will be injected to view model
 */
data class NoteUseCases(
	val getNotesUseCase: GetNotesUseCase,
	val deleteNoteUseCase: DeleteNoteUseCase,
	val addNoteUseCase: AddNoteUseCase
)
