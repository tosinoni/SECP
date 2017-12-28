angular.module('SECP')
  .controller('HomeController', ['$scope', '$webCrypto', 'uuid', function ($scope, $webCrypto, uuid) {
    var fingerprint = new Fingerprint().get();
    console.log(fingerprint);

    console.log(uuid.v4());



    //Generate your ECDH private key using a shortcut function.
//        $webCrypto.generate({name: 'alice'})
//        .success(
//            function(aliceKeyName) {
//                console.log("Alice key name is " + aliceKeyName);
//                //Here you can export alice's public key to send to "bob" so he can also agree the keys.
//                var alicePublicKey = $webCrypto.export(aliceKeyName);
//                console.log("Alice public key is " + alicePublicKey);
//
//                //Generate another ECDH private key (we will use the public part of this one)
//                $webCrypto.generate({name: 'bob'})
//                .success(
//                    function(bobKeyName) {
//                        console.log("Bob key name is " + bobKeyName);
//
//                        //We will export the bob's public key.
//                        var bobPublicKey = $webCrypto.export(bobKeyName);
//                        console.log("Bob public key is " + bobPublicKey);
//                        //Now we will import the bob's public key and derive with alice's to generate
//                        //a RSA-GCM key for encrypting and decrypting.
//                        $webCrypto.importAndDerive(aliceKeyName, bobPublicKey)
//                        .success(
//                            function(cryptoKeyName) {
//                                console.log("key derived from alice's key name and Bob's public key " + cryptoKeyName);
//                                //Now we have the CryptoKey that we can use to encrypt and decrypt data.
//                                $webCrypto.encrypt(cryptoKeyName, 'Hello Bob, how you doing?')
//                                .success(
//                                    function(encrypted, iv) {
//                                        //The string has now been encrypted, we will show this to the console.
//                                        //This format (HexString) is safe for XHR and plain text.
//                                        console.log('encrypted', encrypted);
//                                        //Now we will decrypt the data
//                                        console.log("initialization vector is " + iv);
//                                        $webCrypto.decrypt(cryptoKeyName, encrypted, iv)
//                                        .success(
//                                            function(decrypted) {
//                                                //The data has been decrypted and converted to string
//                                                console.log('decrypted', decrypted);
//                                            }
//                                        )
//                                    }
//                                )
//                            }
//                        )
//                    }
//                )
//            }
//        );
  }]);
