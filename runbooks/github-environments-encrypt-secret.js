const sodium = require('libsodium-wrappers')

const secret = ''
const key = ''

sodium.ready.then(() => {
  let binkey = sodium.from_base64(key, sodium.base64_variants.ORIGINAL)
  let binsec = sodium.from_string(secret)

  let encBytes = sodium.crypto_box_seal(binsec, binkey)
  let output = sodium.to_base64(encBytes, sodium.base64_variants.ORIGINAL)

  console.log(output)
});
