package nl.frankkie.mobifreebackuptool.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat


object PackagesUtil {
    /**
     * Get a list of installed packages on the device.
     *
     * @param context The context to use for getting the package manager.
     * @return A list of installed packages.
     */
    fun getInstalledPackages(context: Context): List<PackageInfo> {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.QUERY_ALL_PACKAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Permission QUERY_ALL_PACKAGES not granted")
            return emptyList()
        }
        return context.packageManager.getInstalledPackages(0)
    }

    fun getListOfInstalledPackageNames(context: Context): List<String> {
        val installedPackages = getInstalledPackages(context)
        return installedPackages.map { it.packageName }
    }

    fun getListOfMyPackageInfo(context: Context): List<MyPackageInfo> {
        val installedPackages = getInstalledPackages(context)
        return installedPackages.map { MyPackageInfo.fromPackageInfo(it, context.packageManager) }
    }

    fun getIconAsDrawable(context: Context, packageName: String): Drawable? {
        val packageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.applicationInfo?.loadIcon(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package not found: $packageName", e)
        }
        return null
    }

    const val TAG = "MFBT"
}

data class MyPackageInfo(
    val displayName: String,
    val packageName: String,
    val versionName: String,
    val isSystemApp: Boolean = false,
) {
    companion object {
        fun fromPackageInfo(
            packageInfo: PackageInfo,
            packageManager: PackageManager
        ): MyPackageInfo {
            return MyPackageInfo(
                displayName = packageInfo.applicationInfo?.loadLabel(packageManager).toString(),
                packageName = packageInfo.packageName,
                versionName = packageInfo.versionName ?: "N/A",
                isSystemApp = ((packageInfo.applicationInfo?.flags ?: 0) and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
            )
        }
    }
}

