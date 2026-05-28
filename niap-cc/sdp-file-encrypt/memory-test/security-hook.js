// Frida script to monitor cryptographic operations in com.android.niapsec.demo

console.log("Frida script loaded. Starting hooks...");

Java.perform(function() {    // Helper function to safely convert Java byte[] to a hex string
    function simpleHexDump(javaByteArray) {
        if (!javaByteArray) {
            return "null";
        }
        try {
            const bytes = Java.array('byte', javaByteArray);
            let hexString = "";
            for (let i = 0; i < bytes.length; i++) {
                if (i > 0 && i % 16 === 0) {
                    hexString += "\n";
                }
                const hex = ('0' + (bytes[i] & 0xFF).toString(16)).slice(-2);
                hexString += hex + " ";
            }
            return hexString.trim();
        } catch (e) {
            return "Error dumping bytes: " + e.toString();
        }
    }

    // --- 1. Hook Cipher.init() to inspect keys ---
    const Cipher = Java.use('javax.crypto.Cipher');
    Cipher.init.overload('int', 'java.security.Key').implementation = function(opmode, key) {
        console.log("\n[+] Cipher.init(opmode, key) hooked!");
        const mode = (opmode === 1) ? "ENCRYPT_MODE" : "DECRYPT_MODE";
        console.log("    - opmode: " + mode);

        if (key) {
            console.log("    - Key class: " + key.$className);
            console.log("    - Key algorithm: " + key.getAlgorithm());

            try {
                const keyBytes = key.getEncoded();
                if (keyBytes) {
                    console.log("    - ★★★ KEY MATERIAL FOUND ★★★:");
                    console.log("      " + simpleHexDump(keyBytes));
                } else {
                    console.log("    - key.getEncoded() returned null. Key material not accessible.");
                }
            } catch (e) {
                console.log("    - Failed to get key material: " + e.toString());
                console.log("    - This is expected for hardware-backed keys.");
            }
        } else {
            console.log("    - Key is null.");
        }
        return this.init(opmode, key);
    };

    // --- 2. Hook Cipher.doFinal() to inspect data ---
    Cipher.doFinal.overload('[B').implementation = function(input) {
        if (!input || input.length === 0) {
            // console.log("\n[+] Cipher.doFinal(byte[]) hooked with empty input. Passing through...");
            return this.doFinal(input);
        }

        console.log("\n[+] Cipher.doFinal(byte[]) hooked!");

        const opmode = this.getIV() ? "ENCRYPT" : "DECRYPT"; // A simple guess
        console.log("    - Guessed operation: " + opmode);
        const inputArray = Java.array('byte', input);
        if(input.$className == undefined){
            console.log("    - Input is not a Java array.");
            return this.doFinal(input);
        }
        //console.log("    - Input length: " + input.$className);
        console.log("    - Input data (first 32 bytes):\n      " + simpleHexDump(inputArray.slice(0, 32)));

        const result = this.doFinal(input);
        const resultArray = Java.array('byte', result);
        console.log("    - Output data (first 32 bytes):\n      " + simpleHexDump(resultArray.slice(0, 32)));
        return result;
    };

    // --- 3. Hook SharedPreferences to monitor writes ---
    const EditorImpl = Java.use('android.app.SharedPreferencesImpl$EditorImpl');
    EditorImpl.putString.implementation = function(key, value) {
        console.log("\n[+] SharedPreferences.putString() hooked!");
        console.log("    - Key: " + key);
        console.log("    - Value (first 100 chars): " + (value ? value.substring(0, 100) : "null"));

        if (key === "hybrid_public_keyset") {
            console.log("    - ★★★ Public keyset is being written to SharedPreferences! ★★★");
        }
        return this.putString(key, value);
    };


    // --- 4. Hook KeyStore operations ---
    const KeyStore = Java.use('java.security.KeyStore');
    KeyStore.getKey.implementation = function(alias, password) {
        console.log("\n[+] KeyStore.getKey() hooked!");
        console.log("    - Alias: " + alias);
        return this.getKey(alias, password);
    };

    KeyStore.deleteEntry.implementation = function(alias) {
        console.log("\n[+] KeyStore.deleteEntry() hooked!");
        console.log("    - Deleting key with alias: " + alias);

        if (alias.startsWith("raw_provider_key_")) {
            console.log("    - ★★★ RawProvider key is being deleted. ★★★");
        }
        this.deleteEntry(alias);
    };

    console.log("\n[+] All hooks are in place. Waiting for app activity...");
});

