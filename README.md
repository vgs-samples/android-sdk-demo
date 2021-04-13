## VGS Collect Android SDK Demo

This examples shows how easily you can integrate <a href="https://github.com/verygoodsecurity/vgs-collect-android">VGS Collect Android SDK</a> 
into your application and secure sensitive data with us.

<p align="center">
    <img src="./android-sdk-demo.gif" width="250">
</p>

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

#### Step 3

Find ``MainActivity.kt`` and set variable VAULT_ID.
<a href="https://www.verygoodsecurity.com/docs/terminology/nomenclature#vault" target="_blank">vault id</a>.  
Your current ``<vault_id>`` you can find in <a href='https://dashboard.verygoodsecurity.com/' target="_blank">Dashboard</a>

#### Step 4 

Run the application and submit the form then 
go to the Logs tab on a Dashboard find request and secure a payload. 
Instruction for this step you can find <a href="https://www.verygoodsecurity.com/docs/getting-started/quick-integration#securing-inbound-connection" target="_blank">here</a>.

### Useful links

- <a href="https://www.verygoodsecurity.com/docs/vgs-collect/android-sdk/overview" target="_blank">Documentation</a> 
- <a href="https://github.com/verygoodsecurity/vgs-collect-android" target="_blank">Repo</a> 

### Samples

|Link |Description|
|-----|-----------|
| [SSN Field Sample](https://github.com/verygoodsecurity/android-sdk-demo/tree/master/collect/src/main/java/com/vgscollect/androiddemo/samples/ssn)| This sample demonstrates basic example of how manage SSN Field. |
| [Payment Card Number Sample](https://github.com/verygoodsecurity/android-sdk-demo/tree/master/collect/src/main/java/com/vgscollect/androiddemo/samples/paymentcardnumber)| This sample demonstrates basic example of how use Payment Card Number field. |
| [Multiregional Support Sample](https://github.com/verygoodsecurity/android-sdk-demo/tree/master/collect/src/main/java/com/vgscollect/androiddemo/samples/multiregional)| This sample of Activity shows you how to support different regions(EU, US). |
| [File Provider Sample](https://github.com/verygoodsecurity/android-sdk-demo/tree/master/collect/src/main/java/com/vgscollect/androiddemo/samples/fileprovider)| This sample demonstrates how to use manage different files in VGS Collect SDK(get access to the file, attach to SDK, detach, submit). |
| [Field States Tracking Sample](https://github.com/verygoodsecurity/android-sdk-demo/tree/master/collect/src/main/java/com/vgscollect/androiddemo/samples/states)| This sample shows how to handle fields states. |
| [Multiplexing Sample](https://github.com/verygoodsecurity/android-sdk-demo/tree/master/collect/src/main/java/com/vgscollect/androiddemo/samples/multiplexing)| This sample of Activity shows you how to gather and send data for multiplexing. |

