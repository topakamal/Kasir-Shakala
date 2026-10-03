# Build APK Shakala Bakery dari HP

Paket ini memakai GitHub Actions, jadi Anda tidak perlu PC atau Android Studio.

## Langkah dari HP

1. Buat repository baru di GitHub, misalnya `shakala-bakery-pos`.
2. Upload seluruh isi folder ZIP ini ke repository (termasuk folder `.github`).
3. Buka tab **Actions**, pilih workflow **Build Shakala Bakery APK**, lalu tekan **Run workflow**.
4. Tunggu sampai selesai.
5. Buka hasil workflow, scroll ke bagian **Artifacts**, lalu unduh `shakala-bakery-debug-apk`.
6. Ekstrak ZIP artifact dan instal file APK di Android.

APK memiliki jembatan Bluetooth Classic/SPP untuk printer yang sudah dipairing. Nama printer default yang dicari adalah `RPP02`, tetapi dapat diganti dari Pengaturan.

Untuk sementara, website tetap bisa dijalankan dengan `python3 -m http.server 4173`; fitur Bluetooth Classic hanya tersedia di APK.
