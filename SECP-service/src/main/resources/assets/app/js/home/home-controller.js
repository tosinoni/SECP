angular.module('SECP')
  .controller('HomeController', ['$scope', function ($scope) {
    // The passphrase used to repeatably generate this RSA key.
    var PassPhrase = "The Moon is a Harsh Mistress.";

    // The length of the RSA key, in bits.
    var Bits = 1024;

    var MattsRSAkey = cryptico.generateRSAKey(PassPhrase, Bits);

    console.log(MattsRSAkey);

    var MattsPublicKeyString = cryptico.publicKeyString(MattsRSAkey);

    console.log(MattsPublicKeyString);

    var PlainText = "Matt, I need you to help me with my Starcraft strategy.";

    var EncryptionResult = cryptico.encrypt(PlainText, MattsPublicKeyString);

    console.log(EncryptionResult);

    var cipherText = EncryptionResult.cipher;

    var DecryptionResult = cryptico.decrypt(cipherText, MattsRSAkey);

    console.log(DecryptionResult.plaintext);
  }]);
