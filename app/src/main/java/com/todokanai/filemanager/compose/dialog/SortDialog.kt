package com.todokanai.filemanager.compose.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.presets.dialog.SelectDialog
import com.todokanai.filemanager.viewmodel.compose.dialog.SortDialogViewModel

@Composable
fun SortDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: SortDialogViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val selectedItem: State<String?> = viewModel.selectedItem.collectAsStateWithLifecycle()

    val sortItems = listOf(
        Pair(stringResource(id = R.string.by_default), { viewModel.saveByDefault() }),
        Pair(stringResource(id = R.string.by_name_ascending), { viewModel.saveByNameAscending() }),
        Pair(stringResource(id = R.string.by_name_descending),
            { viewModel.saveByNameDescending() }),
        Pair(stringResource(id = R.string.by_size_ascending), { viewModel.saveBySizeAscending() }),
        Pair(stringResource(id = R.string.by_size_descending),
            { viewModel.saveBySizeDescending() }),
        Pair(stringResource(id = R.string.by_type_ascending), { viewModel.saveByTypeAscending() }),
        Pair(stringResource(id = R.string.by_type_descending),
            { viewModel.saveByTypeDescending() }),
        Pair(stringResource(id = R.string.by_date_ascending), { viewModel.saveByDateAscending() }),
        Pair(stringResource(id = R.string.by_date_descending), { viewModel.saveByDateDescending() })
    )

    SelectDialog(
        modifier = modifier,
        title = context.getString(R.string.sortBy),
        items = sortItems,
        selectedItem = selectedItem.value,
        onDismissRequest = { onDismissRequest() }
    )
}