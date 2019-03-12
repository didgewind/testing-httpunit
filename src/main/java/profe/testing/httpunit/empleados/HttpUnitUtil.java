package profe.testing.httpunit.empleados;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HttpUnitUtil {

	public static InputStream getInputStreamFromString(String s) throws Exception {
		return new ByteArrayInputStream( s.getBytes( "UTF-8" ) );
	}
	
	public static InputStream getEmpleadoNoExistenteAsInputStream() throws Exception {
		return getInputStreamFromString("{\"cif\":\"wert\",\"nombre\":\"Mirkka\",\"apellidos\":\"Touko\",\"edad\":22}");
	}
	
	public static InputStream getEmpleadoYaExistenteAsInputStream() throws Exception {
		return getInputStreamFromString("{\"cif\":\"323452435B\",\"nombre\":\"Mirkka\",\"apellidos\":\"Touko\",\"edad\":22}");
	}
	
	
}
