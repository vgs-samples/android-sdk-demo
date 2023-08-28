# Compare fields content

This sample demonstrates how to use VGS with Jetpack Compose.

## Prerequisites

- VGS Collect SDK v.1.8.1
- Android SDK 21

## How to run

- Set your `<VAULT_ID>` in `ComposeActivity.collect`
- Set `ComposeActivity` as launcher in `AndroidManifest.xml`
```xml
<activity
    android:name=".samples.compose.ComposeActivity"
    android:theme="@style/AppTheme.Compose"
    android:exported="true">
    
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```
- Run `collect`

## Support

If you've found an error in this sample, please submit an issue or write on email: support@verygoodsecurity.com or create Issue on GitHub.