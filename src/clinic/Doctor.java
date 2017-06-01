package clinic;

import java.util.Collection;
import java.util.*;

public class Doctor extends Person {

	private int docID;
	private String specialization;
	private Map<String,Person> patients = new HashMap<>();
	
	public Doctor(String first, String last, String ssn, int docID, String specialization){
		super(first, last, ssn);
//		sono in super()
//		this.first = first;
//		this.last = last;
//		this.ssn = ssn;
		this.docID = docID;
		this.specialization = specialization;
	}
	
	public int getId(){
		return docID;
	}
	
	public String getSpecialization(){
		return specialization;
	}
	
	public Collection<Person> getPatients() {
		return patients.values();
	}
	
	public void addPatient(Person patient) {
		patients.put(patient.getSSN(), patient);
	}

}
