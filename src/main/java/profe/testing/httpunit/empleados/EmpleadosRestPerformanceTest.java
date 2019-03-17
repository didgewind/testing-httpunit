package profe.testing.httpunit.empleados;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import profe.empleados.model.EmpleadoForTest;

/**
 * Test de rendimiento en JUnit 4 para integración con JMeter.
 * Declara un método test que postea un empleado nuevo. El nif
 * lo generamos de forma incremental mediante un atributo estático.
 * El incremento lo hacemos de forma synchronized para que no haya
 * errores
 * @author made
 *
 */
public class EmpleadosRestPerformanceTest {

	private WebConversation wc;
	private static int intCif = 0;

	private interface Constantes {
		String restUrl = "http://localhost:5555/empleados/";
		String JSON_CONTENT_TYPE = "application/json";
		int CONFLICT_STATUS_CODE = 409;
		int OK_STATUS_CODE = 200;
		int CREATED_STATUS_CODE = 201;
	}
	
	@Before
	public void setUp() {
		wc = new WebConversation();
	}

	@Test
	public void postEmpleadoForPerformanceTesting() throws Exception {
		InputStream is = 
				HttpUnitUtil.getEmpleadoAsInputStream(getNuevoEmpleado());
		WebRequest request = new PostMethodWebRequest(Constantes.restUrl, is,
				Constantes.JSON_CONTENT_TYPE);
		WebResponse response = wc.getResponse(request);
		assertEquals(Constantes.CREATED_STATUS_CODE, response.getResponseCode());
	}
	
	private synchronized static int getNextCif() {
		return ++intCif;
	}

	private static EmpleadoForTest getNuevoEmpleado() {
		return new EmpleadoForTest("" + getNextCif(), "Noelia", "Pérez", 22);
	}
	
}