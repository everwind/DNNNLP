package exception;

public class ConnectionLostException extends RuntimeException {

	private static final long serialVersionUID = 134123421L;
	
	private String message;
	
	public ConnectionLostException() {
		super();
	}

	public ConnectionLostException(Throwable cause) {
		super(cause);
	}

	public ConnectionLostException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public ConnectionLostException(String message) {
		super(message);
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
