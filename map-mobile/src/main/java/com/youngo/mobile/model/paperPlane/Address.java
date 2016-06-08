package com.youngo.mobile.model.paperPlane;

public class Address {
	private String local;
	
	private String country;
	
	private String city;
	
	public Address(){
	}
	
	public Address(String country, String city) {
		this.local = "en";
		this.country = country;
		this.city = city;
	}
	
	public Address(String local, String country, String city) {
		this.local = local;
		this.country = country;
		this.city = city;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return "Address [local=" + local + ", country=" + country + ", city=" + city + "]";
	}

	public enum LocationSupportedLanguage{
		ar("ar"), bg("bq"), bn("bn"), ca("ca"), cs("cs"), da("da"), de("de"), el("el"), en("en"), enAU("en-AU"), enGB("en-GB"), es("es"), eu("eu"), 
		fa("fa"), fi("fi"), fil("fil"), fr("fr"), gl("gl"), gu("qu"), hi("hi"), hr("hr"), hu("hu"), id("id"), it("it"), iw("iw"), ja("ja"),
		kn("kn"), ko("ko"), lt("lt"), lv("lv"), ml("ml"), mr("mr"), nl("nl"), no("no"), pl("pl"), pt("pt"), ptBR("pt-BR"), ptPT("pt-PT"), ro("ro"), 
		ru("ru"), sk("sk"), sl("sl"), sr("sr"), sv("sv"), ta("ta"), te("te"), th("th"), tl("tl"), tr("tr"), uk("uk"), vi("vi"), zh("zh-CN"), zhCN("zh-CN"), zhTW("zh-TW");
		private String value;
		private LocationSupportedLanguage(String value){
			this.value = value;
		}
		
		public String getValue(){
			return value;
		}
		
		
	}
}
