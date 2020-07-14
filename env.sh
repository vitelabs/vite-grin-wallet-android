#ndk 20

export RUST_BACKTRACE=full
export PATH=/home/xirtam/Documents/android-ndk-r20/toolchains/llvm/prebuilt/linux-x86_64/bin:$PATH
export PATH=/home/xirtam/Documents/ndkstandalone/arm64/bin:$PATH
export PATH=/home/xirtam/Documents/ndkstandalone/arm/bin:$PATH
export PATH=/home/xirtam/Documents/ndkstandalone/x86/bin:$PATH
export OPENSSL_DIR=/usr/local/ssl/
cd Documents/Vite_GrinWallet-android/rust/
cargo build --target aarch64-linux-android --release
CC=arm-linux-androideabi-clang cargo build --target armv7-linux-androideabi --release

cd /home/xirtam/Documents/ndkstandalone/
rm -rf *

/home/xirtam/Documents/android-ndk-r20/build/tools/make_standalone_toolchain.py --api 26 --arch arm64 --install-dir arm64
/home/xirtam/Documents/android-ndk-r20/build/tools/make_standalone_toolchain.py --api 26 --arch arm --install-dir arm
/home/xirtam/Documents/android-ndk-r20/build/tools/make_standalone_toolchain.py --api 26 --arch x86 --install-dir x86


#gen standalone
export RUST_BACKTRACE=full

cd /home/xirtam/Documents/ndkstandalone/
rm -rf *

/home/xirtam/Documents/android-ndk-r18b/build/tools/make_standalone_toolchain.py --api 26 --arch arm64 --install-dir arm64
/home/xirtam/Documents/android-ndk-r18b/build/tools/make_standalone_toolchain.py --api 26 --arch arm --install-dir arm
/home/xirtam/Documents/android-ndk-r18b/build/tools/make_standalone_toolchain.py --api 26 --arch x86 --install-dir x86

rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android

cargo build --target aarch64-linux-android --release
cargo build --target armv7-linux-androideabi --release
cargo build --target i686-linux-android --release

cargo build --target aarch64-linux-android --release
CC=arm-linux-androideabi-clang cargo build --target armv7-linux-androideabi --release




