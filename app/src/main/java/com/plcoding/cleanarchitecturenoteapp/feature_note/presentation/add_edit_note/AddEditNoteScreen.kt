package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.plcoding.cleanarchitecturenoteapp.R
import com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun AddEditNoteScreen(
	navController: NavController,
	noteColor: Int,
	viewModel: AddEditNoteViewModel = hiltViewModel(),
) {
	//// Title State ////
	val titleState = viewModel.noteTitle.value
	//// Content State ////
	val contentSate = viewModel.noteContent.value

	//// Scaffold State ////
	val scaffoldState = rememberScaffoldState()

	//// Background animation ////
	val noteBackGroundAnimatable = remember {
		Animatable(
			Color(
				if (noteColor != -1) noteColor else viewModel.noteColor.value
			)
		)
	}

	//// Coroutine scope ////
	val scope = rememberCoroutineScope()

	//// Collect event flow ////
	LaunchedEffect(key1 = true) {
		viewModel.eventFlow.collectLatest { event ->

			when (event) {
				//// Error ////
				is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
					scaffoldState.snackbarHostState.showSnackbar(
						message = event.message
					)
				}
				//// Success ////
				is AddEditNoteViewModel.UiEvent.SaveNote -> {
					navController.navigateUp()
				}

			}

		}
	}

	//// Scaffold ////
	Scaffold(

		//// FAB ////
		floatingActionButton = {
			FloatingActionButton(
				//// FAB onClick ////
				onClick = {
					viewModel.onEvent(event = AddEditNoteEvent.SaveNote)
				},
				//// FAB BG ////
				backgroundColor = MaterialTheme.colors.primary
			) {
				//// FAB Icon ////
				Icon(
					imageVector = Icons.Default.Save,
					contentDescription = stringResource(R.string.save_note)
				)
			}
		},

		//// Scaffold state ////
		scaffoldState = scaffoldState,

		) {

		//// Content ////
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(color = noteBackGroundAnimatable.value)
		) {

			//// Color Selection ////
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(all = 8.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				//// For each color ////
				Note.noteColors.forEach { color ->
					val colorInt = color.toArgb()
					//// Color container ////
					Box(
						modifier = Modifier
							.size(size = 50.dp)
							.shadow(
								elevation = 15.dp,
								shape = CircleShape
							)
							.clip(shape = CircleShape)
							.background(color = color)
							.border(
								width = 3.dp,
								//// If selected black else transparent ////
								color = if (viewModel.noteColor.value == colorInt) {
									Color.Black
								} else Color.Transparent,
								shape = CircleShape
							)
							// When clicked the content background will animate
							.clickable {
								scope.launch {
									noteBackGroundAnimatable.animateTo(
										targetValue = Color(colorInt),
										animationSpec = tween(
											durationMillis = 500
										)
									)
								}
								//// Save color ////
								viewModel.onEvent(event = AddEditNoteEvent.ChangeColor(color = colorInt))
							}
					)
				}
			}

			Spacer(modifier = Modifier.height(16.dp))

			//// Note Title ////
			TransparentHintTextField(text = titleState.text,
				hint = titleState.hint,
				onValueChange = {
					viewModel.onEvent(event = AddEditNoteEvent.EnteredTitle(value = it))
				},
				onFocusChange = {
					viewModel.onEvent(event = AddEditNoteEvent.ChangeTitleFocus(focusState = it))
				},
				isHintVisible = titleState.isHintVisible,
				singleLine = true,
				textStyle = MaterialTheme.typography.h5
			)

			Spacer(modifier = Modifier.height(16.dp))

			//// Note Content ////
			TransparentHintTextField(text = titleState.text,
				hint = contentSate.hint,
				onValueChange = {
					viewModel.onEvent(event = AddEditNoteEvent.EnteredContent(value = it))
				},
				onFocusChange = {
					viewModel.onEvent(event = AddEditNoteEvent.ChangeContentFocus(focusState = it))
				},
				isHintVisible = contentSate.isHintVisible,
				textStyle = MaterialTheme.typography.body1,
				modifier = Modifier
					.fillMaxHeight()
			)

		}
	}
}