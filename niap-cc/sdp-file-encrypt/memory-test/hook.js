console.log("Frida script loaded. Waiting for Cipher.init()...");

Java.perform(function() {
    const Cipher = Java.use('javax.crypto.Cipher');

    Cipher.init.overload('int', 'java.security.Key').implementation = function(opmode, key) {
        console.log("\n[+] Cipher.init(opmode, key) hooked!");

        let mode = (opmode === 1) ? "ENCRYPT_MODE" : "DECRYPT_MODE";
        console.log("    - opmode: " + mode);

        if (key) {
            console.log("    - Key algorithm: " + key.getAlgorithm());

            try {
                const keyBytes = key.getEncoded();
                if (keyBytes) {
                    console.log("    - ★★★ KEY MATERIAL FOUND ★★★:");
                    console.log(simpleHexDump(keyBytes, { ansi: true }));
                } else {
                    console.log("    - key.getEncoded() returned null. Key material is not accessible.");
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
    function simpleHexDump(javaByteArray) {
        const bytes = Java.array('byte', javaByteArray);

        let hexString = "";
        for (let i = 0; i < bytes.length; i++) {
            if (i > 0 && i % 16 === 0) {
                hexString += "\n";
            }
            const hex = ('0' + (bytes[i] & 0xFF).toString(16)).slice(-2);
            hexString += hex + " ";
        }
        return hexString;
    }
});

