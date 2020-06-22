

# Mzaalo Android SDK
This is the official documentation for the integration of Mzaalo android SDKs in any android application with a valid partner code.

## Table of contents

 - [Overview](#overview)
 - [Installation](#installation)
	 - [Requirements](#requirements)
	 - [Configuration](#configuration)
- [Getting Started](#getting-started)
- [Features and Implementation](#features-and-implementation)
	- [auth](#auth)
		- [Login](#login)
		- [Logout](#logout)
	- [rewards](#rewards)
		- [Register Rewards Action](#register-rewards-action)
		- [Fetch Reward Balance](#fetch-reward-balance)
	- [player](#player)
- [Sequence flow](#sequence-flow)

## Overview
Mzaalo SDKs have three modules:

 1. **mzaalo-auth** : This module contains authentication features like login, logout, etc.
 2. **mzaalo-rewards** : This module contains all the authentication features, plus features of rewards like adding rewards, fetching balance, etc.
 3. **mzaalo-player**: This module contains all the rewards and auth features, plus a video player inbuild video with a high level implementation of playback controls

All these modules are shippable as separate gradle/maven dependencies.
Structurally, `mzaalo-auth` is the subset of `mzaalo-rewards` and `mzaalo-rewards` is the subset of `mzaalo-player`. This means, any application that includes the dependency for `mzaalo-rewards` automatically gets the functionality for `mzaalo-auth` out of the box. Also, any application that includes the dependency of `mzaalo-player` automatically gets the functionality for `mzaalo-auth` and `mzaalo-rewards` out of the box.

Following is the pictorial representation of the dependencies and their subsets:
![Dependency of modules](https://xfinitesite.blob.core.windows.net/flow-diagrams/venn-sdk-mobile.png)

    
## Installation

### Requirements

 - Android 4.1 (API Level 16) and above
 - [AndroidX](https://developer.android.com/jetpack/androidx/)

### Configuration
Add `mzaalo-player` or `mzaalo-rewards` or `mzaalo-auth` to the application level `build.gradle` file:

    dependencies{
	    ...
	    implementation 'com.xfinite.mzaalo:mzaalo-xxxx:0.0.4'
	    ...
    }

where, **`xxxx`** can be `auth` or `rewards` or `player`

Add Mzaalo's Maven url repository in `allprojects` block in your project level `build.gradle` file:

    allprojects {
    ...
      repositories {
      ...
        maven {
          url "https://dl.bintray.com/xfiniteio/MzaaloSDKs"
        }
      }
    }



## Getting Started

The entry point to the SDK is through the `init` function that gets called with a valid partner code, a callback based Mzaalo interface object, and an identifier for the environment type(STAGING or PRODUCTION). Call this function in the `onCreate` function of your Application class.

    MzaaloPlayer.init(context, "YOUR_PARTNER_CODE", initListener, MzaaloEnvironment.XXXX)

Here `initListener` can be either a `MzaaloAuthInitListener` or `MzaaloRewardsInitListener` or `MzaaloPlayerInitListener`(depending upon the dependency included) object with the following definition.

    interface MzaaloPlayerInitListener{
	    fun onSuccess()
	    fun onError(error:String)
    }



`MzaaloEnvironment` is an enum class with the following options:

 - **MzaaloEnvironment.STAGING**
 - **MzaaloEnvironment.PRODUCTION**


## Features and Implementation
### auth
#### Login
Your application should call the `MzaaloAuth.login()` function as soon as the user is identified at your end.

	

    val userMeta=JSONObject()
    userMeta.put(userProperty, value)
    MzaaloAuth.login("UNIQUE_ID_OF_YOUR_USER", userMeta, loginListener)

Here are the valid `userProperty` fields that can put as keys in the `userMeta` json:
|userProperty|Description|Data type|Example|
|--|--|--|--|
|email|Email Address of the user|String|johndoe@example.com|
|phone|Phone number of the user|String|9876543210|
|country_code|Country code of the user's phone number|String|+91, +44|


Here `loginListener` is the object of interface `MzaaloAuthLoginListener` that has the following definition:

    interface MzaaloAuthLoginListener{
	    fun onLoginSuccess(user:User)
	    fun onError(error:String)
    }



#### Logout
Your application should call `MzaaloAuth.logout()` function when the user logs out from your application or when the user identitiy is no longer available to you.

    MzaaloAuth.logout()

### rewards
#### Register Rewards Action
This is a feature that allows the application to register an action to the Mzaalo SDK, that should credit some rewards to the user.

    val eventMeta=JSONObject()
    eventMeta(eventProperty, value)
    MzaaloRewards.registerRewardAction(MzaaloRewardsActionTypes.XXXX, eventMeta, actionListener)


`MzaaloRewardsActionTypes` is an enum class that describes the type of action that the user has performed. The enum has following options:
| Enum Value | Description |
|--|--|
| `MzaaloRewardsActionTypes.CONTENT_VIEWED` | Send this if you want to give rewards to the user for watching content |
| `MzaaloRewardsActionTypes.CHECKED_IN` | Send this if you want to give rewards to the user for **launching the app** or **visiting some section of the app** on a daily basis |
| `MzaaloRewardsActionTypes.SIGNED_UP` | Send this if you want to give reward to the user for signing up on your application. In this case, call this once the above mentioned login function has been successfully executed. |
| `MzaaloRewardsActionTypes.REFERRAL_APPLIED` | When a user applies a referral code on the platform which he/she received from some other user previously. This will credit the rewards to the user who referred the current user. |



Here are the valid `eventProperty` fields that can be put as keys in the `eventMeta` json:
| eventProperty | MzaaloRewardsActionTypes | Description | Data type | Example |
|--|--|--|--|--|
| total_watch_time | `CONTENT_VIEWED` | The duration(in seconds) for which the user has watched the content | Integer | 600, (if the user watched a movie for ten minutes) |
| referee_user_id | `REFERRAL_APPLIED` | Unique user ID of the user at your system who referred the current user | String | abcdefgh |
| referee_user_meta | `REFERRAL_APPLIED` | This is the user meta of the person who referred this user. It's format is same as mentioned in the login function for `userMeta` parameter | Json Object | {"email":"johndoe@example.com"} |


Here `actionListener` is the object of interface `MzaaloRewardsRegisterActionListener` that has the following definition:

    interface MzaaloRewardsRegisterActionListener{
	    fun onActionRegistered()
	    fun onError(error: String)
    }


#### Fetch Reward Balance
Call this function if you want to fetch the balance of the user that is currently logged in.

    MzaaloRewards.getBalance(balanceListener)


Here `balanceListener` is the object of interface `MzaaloRewardsBalanceListener` that has the following definition:

    interface MzaaloRewardsBalanceListener{
	    fun onBalanceFetched(balance: Int?)
	    fun onError(error: String)
    }


### player

## Sequence Flow
### mzaalo-auth
![Sequence flow diagram for authentication flow of Mzaalo SDK](https://xfinitesite.blob.core.windows.net/flow-diagrams/mzaalo-auth.png)


### mzaalo-rewards
![Register Rewards action function of the rewards module of the Mzaalo SDK](https://xfinitesite.blob.core.windows.net/flow-diagrams/mzaalo-rewards-rra.png)

![Fetch rewards balance function of the rewards module of Mzaalo SDK](https://xfinitesite.blob.core.windows.net/flow-diagrams/mzaalo-rewards-frb.png)
