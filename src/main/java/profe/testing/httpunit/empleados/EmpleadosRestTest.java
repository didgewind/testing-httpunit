package profe.testing.httpunit.empleados;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpNotFoundException;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class EmpleadosRestTest {

	private WebConversation wc;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {
		wc = new WebConversation();
	}

	@Test
	public void whenGetEmpleadoNoExistenteStatusCodeNotFound() throws Exception {
		exception.expect(HttpNotFoundException.class);
		WebRequest request = new GetMethodWebRequest("http://localhost:5555/empleados/wert");
		WebResponse response = wc.getResponse(request);
	}
}
