package bi.wekapesa.projet.filters;

public interface SecurityParams {
	
	public static final String JWT_HEADER_NAME = "Authorization";
    public static final String SECRET = "wp@weka-pesa.com";
    public static final long EXPIRATION_ACCESS = 5*60*1000;
    public static final long EXPIRATION_REFRESH = 15*60*1000;
    //public static final long EXPIRATION_ACCESS = 10 * 24 * 3600;
    public static final String HEADER_PREFIX = "Bearer ";

}
