## VGS Collect Android SDK Demo

This examples shows how easily you can integrate <a href="https://github.com/verygoodsecurity/vgs-collect-android">VGS Collect Android SDK</a> 
into your application and secure sensitive data with us.

<p align="center">
    <img src="./android-sdk-demo.gif" width="250">
</p>

## How to run it?

#### Preconditions:

- Preinstalled <a href="https://developer.android.com/studio" target="_blank">Android Studio</a>
- Downloaded <a href="https://developer.android.com/studio" target="_blank">Android SDK</a>
- Set up <a href="https://developer.android.com/studio/run/emulator" target="_blank">emulator</a>
- Start with <a href="https://www.verygoodsecurity.com/">VGS</a>


#### Step 1

Go to your <a href="https://dashboard.verygoodsecurity.com/" target="_blank">VGS organization</a> and establish <a href="https://www.verygoodsecurity.com/docs/getting-started/quick-integration#securing-inbound-connection" target="_blank">Inbound connection</a>.

#### Step 2

Clone demo application repository.

``git clone git@github.com:verygoodsecurity/android-sdk-demo.git``

#### Step 3

Find ``MainActivity.kt`` and replace ``<vault_id>`` with your organization
<a href="https://www.verygoodsecurity.com/docs/terminology/nomenclature#vault" target="_blank">vault id</a>.
Your current ``<vault_id>`` you can find in <a href='https://dashboard.verygoodsecurity.com/' target="_blank">Dashboard</a>

#### Step 4

Run the application and submit the form then
go to the Logs tab on a Dashboard find request and secure a payload.
Instruction for this step you can find <a href="https://www.verygoodsecurity.com/docs/getting-started/quick-integration#securing-inbound-connection" target="_blank">here</a>.

#### Useful links

- <a href="https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk" target="_blank">Documentation</a>
- <a href="https://github.com/verygoodsecurity/vgs-collect-android" target="_blank">GitHub Repository</a>

