const cryptojs = require('crypto-js');
const fs = require('fs');

const abemafresh = [1413502068, 2104980084, 1144534056, 1967279194, 2051549272, 860632952, 1464353903, 1212380503];
const url = "abemafresh://abemafresh/206437t2806498f02ad88f4aecd7aa2083a6374edef/2928e09b0fa322020bbe5d854eb92984";

const part1 = url.split('/')[3];
const part2 = url.split('/')[4];

const hash = cryptojs.HmacSHA256(part1, cryptojs.lib.WordArray.create(abemafresh));
const decryptResult = cryptojs.AES.decrypt(cryptojs.lib.CipherParams.create({
    ciphertext: cryptojs.enc.Hex.parse(part2)
}), hash, {
        mode: cryptojs.mode.ECB,
        padding: cryptojs.pad.NoPadding
    }).toString();

for (var t = new Uint8Array(16), r = 0; 16 > r; r++)
    t[r] = parseInt(decryptResult.substr(2 * r, 2), 16);

const result = [];

for (const i of t) {
    result.push(i.toString(16).length === 1 ? ('0' + i.toString(16)) : i.toString(16));
}

const resultKey = result.join('');
console.log('key = ', resultKey);

const binaryKey = binarize16(resultKey);
fs.writeFile('/tmp/program2.key', new Buffer(binaryKey), (err) => {
    if (err) throw err;
    console.log('The file has been saved!');
});

function binarize16(str) {
    let a = new Uint8Array(16),
        i = 0

    for (; 16 > i; i++) {
        a[i] = parseInt(str.substr(2 * i, 2), 16)
    }
    return a
}
