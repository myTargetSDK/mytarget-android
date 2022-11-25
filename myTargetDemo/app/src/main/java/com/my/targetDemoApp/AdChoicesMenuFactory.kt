package com.my.targetDemoApp

import android.app.AlertDialog
import android.content.Context
import com.my.target.common.menu.Menu
import com.my.target.common.menu.MenuAction
import com.my.target.common.menu.MenuActionStyle
import com.my.target.common.menu.MenuFactory

class AdChoicesMenuFactory: MenuFactory
{
    override fun createMenu(): Menu {
        return object : Menu {
            private var listener: Menu.Listener? = null
            private var actions: MutableList<MenuAction> = ArrayList()
            private var dialog: AlertDialog? = null

            override fun setListener(listener: Menu.Listener?) {
                this.listener = listener
            }

            override fun addAction(action: MenuAction) {
                actions.add(action)
            }

            override fun present(context: Context) {
                val options = actions.map { if (it.style == MenuActionStyle.CANCEL) "Закрыть" else it.title }

                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setItems(options.toTypedArray()) { _, which -> listener?.onActionClick(actions[which]) }
                alertDialog.setCancelable(false)

                dialog = alertDialog.create()
                dialog?.show()
            }

            override fun dismiss() {
                dialog?.dismiss()
            }
        }
    }
}