package com.zainal.android.catalogue.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.zainal.android.catalogue.R

/**
 * Implementation of App Widget functionality.
 */
class ImageFavoriteWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.zainal.android.movie.TOAST_ACTION"
        const val UPDATE_ACTION = "com.zainal.android.movie.UPDATE_ACTION"
        const val EXTRA_ITEM = "com.zainal.android.movie.EXTRA_ITEM"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.image_favorite_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, ImageFavoriteWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        fun goUpdateWidget(context: Context) {
            val intent = Intent(context, ImageFavoriteWidget::class.java)
            intent.action = UPDATE_ACTION
            context.sendBroadcast(intent)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            when (intent.action) {
                TOAST_ACTION -> {
                    val title = intent.getStringExtra(EXTRA_ITEM)
                    Toast.makeText(context, "Touched view $title", Toast.LENGTH_SHORT).show()
                }
                UPDATE_ACTION -> {
                    context?.let {
                        val manager = AppWidgetManager.getInstance(it)
                        val ids = manager.getAppWidgetIds(ComponentName(it, ImageFavoriteWidget::class.java))
                        manager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
                    }
                }
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}