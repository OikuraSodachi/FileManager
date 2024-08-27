package com.todokanai.filemanager.compose.bottommenucontent

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.todokanai.filemanager.R
import com.todokanai.filemanager.compose.dialog.InfoDialog
import com.todokanai.filemanager.compose.presets.dropdownmenu.MyDropdownMenu
import java.io.File

@Composable
fun BottomPopupMenu(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    zip:()->Unit,
    selectAll:()->Unit,
    unselectAll:()->Unit,
    selected:()->Array<File>
){
    val context = LocalContext.current
    val infoDialog = remember{ mutableStateOf(false) }


    /** more 버튼 내용물 **/
    fun contents(context: Context, items:Array<File>):List<Pair<String,()->Unit>>{
        val result = mutableListOf<Pair<String,()->Unit>>(
            Pair(context.getString(R.string.bottom_popup_info),{infoDialog.value = true}),
            Pair(context.getString(R.string.bottom_popup_menu_zip),{zip()}),
            Pair(context.getString(R.string.select_all),{selectAll()}),
            Pair(context.getString(R.string.unselect_all),{unselectAll()})

        )
        //-------------

        if(items.size == 1){        // --> single item
            val item = items.first()
            result.add(Pair(context.getString(R.string.bottom_popup_rename),{}))
        }
        return result
    }

    MyDropdownMenu(
        modifier = modifier,
        contents = contents(context,selected()),
        expanded = expanded.value,
        onDismissRequest = {expanded.value = false}
    )

    if(infoDialog.value){
        InfoDialog(
            onDismiss = {infoDialog.value = false}
        )
    }


}

