package id.shakala.bakery.pos;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.PluginMethod;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

@CapacitorPlugin(name = "ClassicPrinter", permissions = {
  @Permission(alias = "bluetooth", strings = { Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN })
})
public class ClassicPrinterPlugin extends Plugin {
  private static final UUID SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

  @PluginMethod
  public void print(PluginCall call) {
    if (android.os.Build.VERSION.SDK_INT >= 31 && getContext().checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
      requestPermissionForAlias("bluetooth", call, "bluetooth"); return;
    }
    String preferred = call.getString("printerName", "RPP02");
    String text = call.getString("text", "");
    try {
      BluetoothDevice target = null;
      BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
      Set<BluetoothDevice> bonded = adapter.getBondedDevices();
      for (BluetoothDevice d : bonded) if (d.getName() != null && (d.getName().equalsIgnoreCase(preferred) || d.getName().contains(preferred))) { target=d; break; }
      if (target == null) { call.reject("Printer belum dipairing: " + preferred); return; }
      android.bluetooth.BluetoothSocket socket=target.createRfcommSocketToServiceRecord(SPP);
      socket.connect(); OutputStream out=socket.getOutputStream();
      out.write(new byte[]{0x1B,0x40}); out.write(text.getBytes(StandardCharsets.UTF_8)); out.write(new byte[]{0x0A,0x0A,0x0A,0x1D,0x56,0x00}); out.flush(); socket.close();
      JSObject ret=new JSObject(); ret.put("printer",target.getName()); call.resolve(ret);
    } catch(Exception e) { call.reject("Gagal mencetak: " + e.getMessage()); }
  }
}
