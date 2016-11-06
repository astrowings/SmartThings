/**
 *  Test
 *
 *  Copyright © 2016 Phil Maynard
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0                                       */
 	       def urlApache() { return "http://www.apache.org/licenses/LICENSE-2.0" }      /*
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 *	VERSION HISTORY                                    */
 	 def versionNum() {	return "version 0.1" }          /*
 *
 *   31-Oct-2016 : v# - most recent release changes
 *   28-Oct-2016 : v# - previous release changes
 *
*/
definition(
    name: "Test",
    namespace: "astrowings",
    author: "Phil Maynard",
    description: "Test",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


//   ---------------------------
//   ***   APP PREFERENCES   ***

preferences {
	page(name: "pageMain")
    page(name: "pageSettings")
    page(name: "pageUninstall")
}


//   --------------------------------
//   ***   CONSTANTS DEFINITIONS  ***



//   -----------------------------
//   ***   PAGES DEFINITIONS   ***

//TODO: implement href state (exemple: https://github.com/SmartThingsCommunity/Code/blob/master/smartapps/preferences/page-params-by-href.groovy)
def pageMain() {
	dynamicPage(name: "pageMain", title: "Main", install: true, uninstall: false) { //with 'install: true', clicking 'Done' installs/updates the app
    	section(){
        	paragraph "", title: "This SmartApp is used for various tests."
        }
        section("Inputs") {
            input "theSwitches", "capability.switch",
            	title: "Select the switches",
                description: "This is a long input description to demonstrate that it will wrap over multiple lines",
                multiple: true,
                required: false,
                submitOnChange: false
        }
		section() {
            href "pageSettings", title: "App settings", image: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png", required: false
		}
    }
}
    
def pageSettings() {
	dynamicPage(name: "pageSettings", title: "Settings", install: false, uninstall: false) { //with 'install: false', clicking 'Done' goes back to previous page
		section("About") {
        	paragraph "Copyright ©2016 Phil Maynard\n${versionNum()}", title: app.name
            href name: "hrefLicense", title: "License", description: "Apache License", url: urlApache()
            paragraph stateCap(), title: "Memory Usage"
		}
   		section() {
			label title: "Assign a name", defaultValue: "${app.name}", required: false
            href "pageUninstall", title: "", description: "Uninstall this SmartApp", image: "https://cdn0.iconfinder.com/data/icons/social-messaging-ui-color-shapes/128/trash-circle-red-512.png", state: null, required: true
		}
        section("Debugging Options", hideable: true, hidden: true) {
            input "debugging", "bool", title: "Enable debugging", defaultValue: false, required: false, submitOnChange: true
            if (debugging) {
                input "log#info", "bool", title: "Log info messages", defaultValue: true, required: false
                input "log#trace", "bool", title: "Log trace messages", defaultValue: true, required: false
                input "log#debug", "bool", title: "Log debug messages", defaultValue: true, required: false
                input "log#warn", "bool", title: "Log warning messages", defaultValue: true, required: false
                input "log#error", "bool", title: "Log error messages", defaultValue: true, required: false
            }
        }
    }
}

def pageUninstall() {
	dynamicPage(name: "pageUninstall", title: "Uninstall", install: false, uninstall: true) {
		section() {
        	paragraph "CAUTION: You are about to completely remove the SmartApp '${app.name}'. This action is irreversible. If you want to proceed, tap on the 'Remove' button below.",
                required: true, state: null
        }
	}
}


//   ----------------------------
//   ***   APP INSTALLATION   ***

def installed() {
	debug "installed with settings: ${settings}", "trace"
	initialize()
}

def updated() {
    debug "updated with settings ${settings}", "trace"
	unsubscribe()
    //unschedule()
    initialize()
}

def uninstalled() {
    //theLights?.off()
    state.debugLevel = 0
    debug "application uninstalled", "trace"
}

def initialize() {
    state.debugLevel = 0
    debug "initializing", "trace", 1
    //theLights?.off()
    subscribeToEvents()
    debug "initialization complete", "trace", -1
    testStart()
}

def subscribeToEvents() {
    debug "subscribing to events", "trace", 1
	subscribe(theSwitches, "switch", testProperties)
    //subscribe(theSwitches, "switch", switchEvent)
    debug "subscriptions complete", "trace", -1
}

//   --------------------------
//   ***   EVENT HANDLERS   ***

def switchEvent(evt) {
    debug "switchEvent event: ${evt.descriptionText}", "trace", 1
	debug "switch event"
    debug "switchEvent complete", "trace", -1
}

def testProperties(evt) {
	debug "testProperties event: ${evt.descriptionText}", "trace", 1
    debug "eventProperties>data:${evt.data}"
    debug "eventProperties>description:${evt.description}"
    debug "eventProperties>descriptionText:${evt.descriptionText}"
    debug "eventProperties>device:${evt.device}"
    debug "eventProperties>displayName:${evt.displayName}"
    debug "eventProperties>deviceId:${evt.deviceId}"
    debug "eventProperties>installedSmartAppId:${evt.installedSmartAppId}"
    debug "eventProperties>name:${evt.name}"
    debug "eventProperties>source:${evt.source}"
    debug "eventProperties>stringValue:${evt.stringValue}"
    debug "eventProperties>unit:${evt.unit}"
    debug "eventProperties>value:${evt.value}"
    debug "eventProperties>isDigital:${evt.isDigital()}"
    debug "eventProperties>isPhysical:${evt.isPhysical()}"
    debug "eventProperties>isStateChange:${evt.isStateChange()}"
	debug "testProperties complete", "trace", -1
}


//   -------------------
//   ***   METHODS   ***

def testStart() {
	debug "executing testStart()", "trace", 1
	runIn(30, toggleSwitches)
	debug "testStart() complete", "trace", -1
}

def toggleSwitches() {
	debug "executing toggleSwitch()", "trace", 1
	if (theSwitches) {
    	theSwitches.each {
        	if (it.currentSwitch == "on") {
            	debug "turning off the ${it.name}"
                it.off()
            } else {
            	debug "turning on the ${it.label}"
                it.on()
            }
        }
    } else {
    	debug "no switches selected"
    }
    //runIn(30, toggleSwitches)
	debug "toggleSwitch() complete", "trace", -1
}

def listDevices() {
	debug "executing listDevices()", "trace", 1
    def switchQty = theSwitches ? theSwitches.size() : 0
    debug "switchQty:$switchQty"
    if (switchQty == 1) {
        debug "1 switch selected"
    } else if (switchQty > 1) {
        debug "${switchQty} switches selected"
    } else {
        debug "no switches selected"
    }
	debug "listDevices() complete", "trace", -1
}

private		C_1()						{ return "this is constant1" }
private		getC_2()					{ return "this is constant2" }
private		getSOME_CONSTANT()			{ return "this is some constant" }
private		getC_SOME_OTHER_CONSTANT()	{ return "this is some other constant" }
private		C_NEW_CONSTANT()			{ return "this is a new constant" }

def debugTest() {
	debug "executing debugTest()", "trace", 1
    debug "constant1 : ${C_1()}"//					-> this is constant1 
    debug "constant2a: ${C_2}"//					-> null
    debug "constant2b: ${c_2}"//					-> this is constant2 
    debug "constant3a: ${SOME_CONSTANT}"//			-> this is some constant
    debug "constant4a: ${c_SOME_OTHER_CONSTANT}"//	-> this is some other constant
    debug "constant4b: ${C_SOME_OTHER_CONSTANT}"//	-> null
    debug "constant5a: ${c_NEW_CONSTANT}"//			-> null
    debug "constant5b: ${C_NEW_CONSTANT}"//			-> null
   	debug "a random number between 4 and 16 could be: ${randomWithRange(4, 16)}"
	debug "debugTest() complete", "trace", -1
}

//   ------------------------
//   ***   COMMON UTILS   ***

/*
 ** see below for idiot-proof version of this **
 
        int randomWithRange(int min, int max)
        {
           int range = (max - min) + 1;     
           return (int)(Math.random() * range) + min;
        }
        
 *
 */

int randomWithRange(int min, int max)
{
   int range = Math.abs(max - min) + 1;     
   return (int)(Math.random() * range) + (min <= max ? min : max);
}

def stateCap(showBytes = true) {
	def bytes = state.toString().length()
	return Math.round(100.00 * (bytes/ 100000.00)) + "%${showBytes ? " ($bytes bytes)" : ""}"
}

def cpu() {
	if (state.lastExecutionTime == null) {
		return "N/A"
	} else {
		def cpu = Math.round(state.lastExecutionTime / 20000)
		if (cpu > 100) {
			cpu = 100
		}
		return "$cpu%"
	}
}


//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//
//   *******************   TEST ZONE  ********************   //
//   Put new code here before moving up into main sections   //
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//

def debug(message, lvl = null, shift = null, err = null) {
	def debugging = settings.debugging
	if (!debugging) {
		return
	}
	lvl = lvl ?: "debug"
	if (!settings["log#$lvl"]) {
		return
	}
	
    def maxLevel = 4
	def level = state.debugLevel ?: 0
	def levelDelta = 0
	def prefix = "║"
	def pad = "░"
	
    //shift is:
	//	 0 - initialize level, level set to 1
	//	 1 - start of routine, level up
	//	-1 - end of routine, level down
	//	 anything else - nothing happens
	
    switch (shift) {
		case 0:
			level = 0
			prefix = ""
			break
		case 1:
			level += 1
			prefix = "╚"
			pad = "═"
			break
		case -1:
			levelDelta = -(level > 0 ? 1 : 0)
			pad = "═"
			prefix = "╔"
			break
	}

	if (level > 0) {
		prefix = prefix.padLeft(level, "║").padRight(maxLevel, pad)
	}

	level += levelDelta
	state.debugLevel = level

	if (debugging) {
		prefix += " "
	} else {
		prefix = ""
	}

    if (lvl == "info") {
        log.info ": :$prefix$message", err
	} else if (lvl == "trace") {
        log.trace "::$prefix$message", err
	} else if (lvl == "warn") {
		log.warn "::$prefix$message", err
	} else if (lvl == "error") {
		log.error "::$prefix$message", err
	} else {
		log.debug "$prefix$message", err
	}
}