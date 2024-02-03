package com.annuityfarm.annuityfarmapp.ext;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.annuityfarm.annuityfarmapp.R


/**
 * Created by Chathura Wijesinghe
 * cdanasiri@gmail.com
 */

//https://snippets.cdan.me/kotlin-extensions/alertdialog-extensions
inline fun Activity.alert(cancelable:Boolean = true,showIcon: Int = 0,title: CharSequence? = null, message: CharSequence? = null, func: AlertDialogHelper.() -> Unit): AlertDialog {
    return AlertDialogHelper(this, title, message,showIcon,cancelable).apply {
        func()

    }.create()
}

inline fun Activity.alert(cancelable:Boolean = true,showIcon: Int = 0,title: CharSequence? = null, titleColor:Int? = null, message: CharSequence? = null, messageColor:Int? = null, func: AlertDialogHelper.() -> Unit): AlertDialog {
    return AlertDialogHelper(this, title, message,showIcon,cancelable,titleColor=titleColor,messageColor=messageColor).apply {
        func()

    }.create()
}

inline fun Fragment.alert(cancelable:Boolean = true,showIcon: Int = 0,title: CharSequence? = null, titleColor:Int? = null, message: CharSequence? = null, messageColor:Int? = null, func: AlertDialogHelper.() -> Unit): AlertDialog {
    return AlertDialogHelper(requireContext(), title, message,showIcon,cancelable,titleColor=titleColor,messageColor=messageColor).apply {
        func()

    }.create()
}

inline fun Fragment.alert(cancelable:Boolean = true,showIcon: Int = 0,title: CharSequence? = null, message: CharSequence? = null, func: AlertDialogHelper.() -> Unit): AlertDialog {
    return AlertDialogHelper(requireContext(), title, message,showIcon,cancelable).apply {
        func()
    }.create()
}

inline fun Fragment.alert(cancelable:Boolean = true,showIcon: Int = 0,titleResource: Int = 0, messageResource: Int = 0, func: AlertDialogHelper.() -> Unit): AlertDialog {
    val title = if (titleResource == 0) null else getString(titleResource)
    val message = if (messageResource == 0) null else getString(messageResource)
    return AlertDialogHelper(requireContext(), title, message,showIcon,cancelable).apply {
        func()
    }.create()
}

@SuppressLint("InflateParams")
class AlertDialogHelper(context: Context, title: CharSequence?, message: CharSequence?, showIcon: Int = 0,cancelable:Boolean = true, titleColor:Int? = null, messageColor:Int? = null) {

    private val dialogView: View by lazyFast {
        LayoutInflater.from(context).inflate(R.layout.dialog_info, null)
    }

    private val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        .setView(dialogView)


    private val title: TextView by lazyFast {
        dialogView.findViewById(R.id.dialogInfoTitleTextView)

    }

    private val myTitleColor = titleColor
    private val myMessageColor = messageColor

    private val icon: Int = showIcon

    private val message: TextView by lazyFast {
        dialogView.findViewById(R.id.dialogInfoMessageTextView)
    }

    private val positiveButton: Button by lazyFast {
        dialogView.findViewById(R.id.dialogInfoPositiveButton)
    }

    private val negativeButton: Button by lazyFast {
        dialogView.findViewById(R.id.dialogInfoNegativeButton)
    }


    private val dialogInfoImageIcon: ImageView by lazyFast {
        dialogView.findViewById(R.id.dialogInfoImageIcon)
    }



    private var dialog: AlertDialog? = null

    var cancelable: Boolean = cancelable

    init {
        this.title.text = title
        titleColor?.let{this.title.setTextColor(it)}
        this.message.text = message
        messageColor?.let{this.message.setTextColor(it)}
    }


    fun positiveButton(@StringRes textResource: Int, func: (() -> Unit)? = null) {
        with(positiveButton) {
            text = builder.context.getString(textResource)
            setClickListenerToDialogButton(func)
        }
    }

    fun positiveButton(text: CharSequence, func: (() -> Unit)? = null) {
        with(positiveButton) {
            this.text = text
            setClickListenerToDialogButton(func)
        }
    }

    fun negativeButton(@StringRes textResource: Int, func: (() -> Unit)? = null) {
        with(negativeButton) {
            text = builder.context.getString(textResource)
            setClickListenerToDialogButton(func)
        }
    }

    fun negativeButton(text: CharSequence, func: (() -> Unit)? = null) {
        with(negativeButton) {
            this.text = text
            setClickListenerToDialogButton(func)
        }
    }

    fun onCancel(func: () -> Unit) {
        builder.setOnCancelListener { func() }
    }

    fun create(): AlertDialog {
        title.goneIfTextEmpty()
        message.goneIfTextEmpty()
        positiveButton.goneIfTextEmpty()
        negativeButton.goneIfTextEmpty()
        dialogInfoImageIcon.setImageResource(icon)

        myTitleColor?.let{this.title.setTextColor(it)}
        myMessageColor?.let{this.message.setTextColor(it)}

        dialog = builder
            .setCancelable(cancelable)
            .create()

        if (dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(0))
        }

        return dialog!!
    }

    private fun TextView.goneIfTextEmpty() {
        visibility = if (text.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun Button.setClickListenerToDialogButton(func: (() -> Unit)?) {
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }
    }
}

/**
 * Implementation of lazy that is not thread safe. Useful when you know what thread you will be
 * executing on and are not worried about synchronization.
 */
fun <T> lazyFast(operation: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    operation()
}