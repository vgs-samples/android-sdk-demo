## VGS Collect Android SDK Demo

This examples shows how easily you can integrate <a href="https://github.com/verygoodsecurity/vgs-collect-android">VGS Collect Android SDK</a> 
into your application and secure sensitive data.

## How to run it?

### Requirements

- Installed <a href="https://developer.android.com/studio" target="_blank">Android Studio</a>
- Installed <a href="https://developer.android.com/studio/run/emulator" target="_blank">emulator</a>
- Organization with <a href="https://www.verygoodsecurity.com/">VGS</a>


#### Step 1

Go to your <a href="https://dashboard.verygoodsecurity.com/" target="_blank">VGS organization</a> and establish <a href="https://www.verygoodsecurity.com/docs/getting-started/quick-integration#securing-inbound-connection" target="_blank">Inbound connection</a>. 

#### Step 2

Clone demo application repository.

``git clone git@github.com:verygoodsecurity/android-sdk-demo.git``

#### Step 4

Find ``MainActivity.kt`` in line **17** replace ``<vault_id>`` with your organization
 <a href="https://www.verygoodsecurity.com/docs/terminology/nomenclature#vault" target="_blank">vault id</a>. 
 
### Step 5 

Run application and submit the form then 
go to the Logs tab on a Dashboard find request and secure a payload. 
Instruction for this step you can find <a href="https://www.verygoodsecurity.com/docs/getting-started/quick-integration#securing-inbound-connection" target="_blank">here</a>.

### Useful links

- <a href="https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk" target="_blank">Documentation</a> 
- <a href="https://github.com/verygoodsecurity/vgs-collect-android" target="_blank">Repo</a> 

