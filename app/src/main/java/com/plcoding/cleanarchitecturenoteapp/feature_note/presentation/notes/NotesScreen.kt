package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components.NoteItem
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.components.OrderSection
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
	navController: NavController,
	viewModel: NotesViewModel = hiltViewModel(),
) {

	val state = viewModel.state.value
	val scaffoldState = rememberScaffoldState()
	val scope = rememberCoroutineScope()

	//// Scaffold ////
	Scaffold(

		//// FAB ////
		floatingActionButton = {
			FloatingActionButton(
				//// FAB onClick ////
				onClick = {
					navController.navigate(route = Screen.AddEditNoteScreen.route)
				},
				//// FAB BG ////
				backgroundColor = MaterialTheme.colors.primary
			) {
				//// FAB Icon ////
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = stringResource(R.string.add_note)
				)
			}
		},

		//// scaffoldState ////
		scaffoldState = scaffoldState
	) {

		//// Scaffold Content ////
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {

			//// First Row ////
			Row(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {

				//// Your note Text ////
				Text(
					text = stringResource(R.string.your_note),
					style = MaterialTheme.typography.h4
				)

				//// Icon Button ////
				IconButton(
					onClick = {
						viewModel.onEvent(event = NotesEvent.ToggleOrderSection)
					},
					modifier = Modifier
				) {
					Icon(
						imageVector = Icons.Default.Sort,
						contentDescription = stringResource(R.string.sort)
					)
				}
			}

			//// Order Section ////
			AnimatedVisibility(
				visible = state.isOrderSectionVisible,
				enter = fadeIn() + slideInVertically(),
				exit = fadeOut() + slideOutVertically()
			) {
				OrderSection(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 16.dp),
					noteOrder = state.noteOrder,
					onOrderChange = { newNoteOrder ->
						viewModel.onEvent(
							event = NotesEvent.Order(
								noteOrder = newNoteOrder
							)
						)
					}
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			//// Notes ////
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
			) {
				items(
					items = state.notes,
				) { note ->
					NoteItem(
						note = note,
						modifier = Modifier
							.fillMaxWidth()
							//// Each note clickable  ////
							.clickable {
								navController.navigate(
									route = Screen.AddEditNoteScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
								)
							},
						onDeleteClick = {
							viewModel.onEvent(event = NotesEvent.DeleteNote(note = note))
							scope.launch {
								val result = scaffoldState.snackbarHostState.showSnackbar(
									message = "Note Deleted",
									actionLabel = "Undo"
								)
								if (result == SnackbarResult.ActionPerformed) {
									viewModel.onEvent(event = NotesEvent.RestoreNote)
								}
							}
						}
					)
					//// Space between notes ////
					Spacer(modifier = Modifier.height(16.dp))
				}
			}

		}

	}

}