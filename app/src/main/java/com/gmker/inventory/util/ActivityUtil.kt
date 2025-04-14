package com.gmker.inventory.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.reflect.InvocationTargetException
import java.util.LinkedList

object ActivityUtil {

    private val activityLifecycle = ActivityLifecycleImpl()

    val topActivity: Activity?
        get() = activityLifecycle.topActivity

    fun init(app: Application?) {
        if (app == null) {
            applicationByReflect.registerActivityLifecycleCallbacks(activityLifecycle)
        } else {
            app.registerActivityLifecycleCallbacks(activityLifecycle)
        }
    }

    private val applicationByReflect: Application
        get() {
            try {
                @SuppressLint("PrivateApi") val activityThread = Class.forName("android.app.ActivityThread")
                val thread = activityThread.getMethod("currentActivityThread").invoke(null)
                val app = activityThread.getMethod("getApplication").invoke(thread)
                    ?: throw NullPointerException("u should init first")
                return app as Application
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            throw NullPointerException("u should init first")
        }

    class ActivityLifecycleImpl : Application.ActivityLifecycleCallbacks {
        private val mActivityList = LinkedList<Activity>()
        private var mForegroundCount = 0
        private var mConfigCount = 0
        private var mIsBackground = false
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            setTopActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            if (!mIsBackground) {
                setTopActivity(activity)
            }
            if (mConfigCount < 0) {
                ++mConfigCount
            } else {
                ++mForegroundCount
            }
        }

        override fun onActivityResumed(activity: Activity) {
            setTopActivity(activity)
            if (mIsBackground) {
                mIsBackground = false
            }
        }

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            if (activity.isChangingConfigurations) {
                --mConfigCount
            } else {
                --mForegroundCount
                if (mForegroundCount <= 0) {
                    mIsBackground = true
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
        }

        val topActivity: Activity?
            get() {
                if (!mActivityList.isEmpty()) {
                    val topActivity = mActivityList.last
                    if (topActivity != null) {
                        return topActivity
                    }
                }
                val topActivityByReflect = topActivityByReflect
                topActivityByReflect?.let { setTopActivity(it) }
                return topActivityByReflect
            }

        private fun setTopActivity(activity: Activity) {
            if (mActivityList.contains(activity)) {
                if (mActivityList.last != activity) {
                    mActivityList.remove(activity)
                    mActivityList.addLast(activity)
                }
            } else {
                mActivityList.addLast(activity)
            }
        }

        private val topActivityByReflect: Activity?
            get() {
                try {
                    @SuppressLint("PrivateApi") val activityThreadClass = Class.forName("android.app.ActivityThread")
                    val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread").invoke(null)
                    val mActivityListField = activityThreadClass.getDeclaredField("mActivityList")
                    mActivityListField.isAccessible = true
                    val activities = mActivityListField[currentActivityThreadMethod] as Map<*, *>
                    for (activityRecord in activities.values) {
                        val activityRecordClass: Class<*> = activityRecord!!::class.java
                        val pausedField = activityRecordClass.getDeclaredField("paused")
                        pausedField.isAccessible = true
                        if (!pausedField.getBoolean(activityRecord)) {
                            val activityField = activityRecordClass.getDeclaredField("activity")
                            activityField.isAccessible = true
                            return activityField[activityRecord] as Activity
                        }
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: NoSuchFieldException) {
                    e.printStackTrace()
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                return null
            }
    }
}