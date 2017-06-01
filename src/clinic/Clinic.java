package clinic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;



import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.sun.glass.ui.Size;

import sun.swing.text.CountingPrintable;

public class Clinic {

	Map<String,Person> patients = new HashMap<>();
	Map<Integer,Doctor> doctors = new HashMap<>();

	public void addPatient(String first, String last, String ssn) {
		patients.put(ssn, new Person(first, last, ssn));
	}

	public void addDoctor(String first, String last, String ssn, int docID, String specialization) {
		doctors.put(docID, new Doctor(first, last, ssn, docID, specialization));
	}

	public Person getPatient(String ssn) throws NoSuchPatient {
		return patients.get(ssn);
	}

	public Doctor getDoctor(int docID) throws NoSuchDoctor {
		if (doctors.get(docID) == null) 
			throw new NoSuchDoctor();
		return doctors.get(docID);
	}

	public void assignPatientToDoctor(String ssn, int docID) throws NoSuchPatient, NoSuchDoctor {
		Person patient = patients.get(ssn);
		Doctor doctor = doctors.get(docID);

		patient.addDoctor(doctor);
		doctor.addPatient(patient);		
	}

	/**
	 * returns the collection of doctors that have no patient at all, sorted in alphabetic order.
	 */
	Collection<Doctor> idleDoctors(){
		return 
				doctors.values().stream()
				.filter(d -> d.getPatients().isEmpty())
				.sorted(new Comparator<Doctor>() {
					@Override
					public int compare(Doctor d1, Doctor d2){
						return d1.getLast().compareTo(d2.getLast());
					}
				})
				.collect(Collectors.toList())
				;
	}

	/**
	 * returns the collection of doctors that a number of patients larger than the average.
	 */
	Collection<Doctor> busyDoctors(){
		OptionalDouble avgOptional = doctors.values().stream()
					.mapToDouble(d -> d.getPatients().size())
					.average();
		
		double avg = avgOptional.getAsDouble();
		
		return doctors.values().stream()
					.filter(d -> d.getPatients().size() > avg)
					.collect(Collectors.toSet())
				;
	}

	/**
	 * returns list of strings
	 * containing the name of the doctor and the relative number of patients
	 * with the relative number of patients, sorted by decreasing number.<br>
	 * The string must be formatted as "<i>### : ID SURNAME NAME</i>" where <i>###</i>
	 * represent the number of patients (printed on three characters).
	 */
	Collection<String> doctorsByNumPatients(){
		Collection<String> strings = new ArrayList<>();

		Collection<Doctor> sortedDoctors = 
				doctors.values().stream()
					.sorted(new Comparator<Doctor>(){
						@Override
						public int compare(Doctor d1, Doctor d2){
							return d1.getPatients().size() - d2.getPatients().size();
						}
					})
					.collect(Collectors.toList());
		sortedDoctors.forEach(d ->{
			String s = "" + String.format("%3d", d.getPatients().size()) + " : " + d.getId() + " " + d.getLast() + " " + d.getFirst();
			strings.add(s);
		});
		
		return strings;
	}

	/**
	 * computes the number of
	 * patients per (their doctor's) specialization.
	 * The elements are sorted first by decreasing count and then by alphabetic specialization.<br>
	 * The strings are structured as "<i>### - SPECIALITY</i>" where <i>###</i>
	 * represent the number of patients (printed on three characters).
	 */
	public Collection<String> countPatientsPerSpecialization(){
		
		Map<String, Integer> specializationNum = new HashMap<>();
		Collection<String> specializationStrings = new ArrayList<String>();
		
		doctors.values().forEach(d->{
			int num = 0;
			if( specializationNum.get(d.getSpecialization()) != null ){ 
				num = specializationNum.get(d.getSpecialization());
			}
			specializationNum.put(d.getSpecialization(), num + d.getPatients().size());
			//specializationStrings.add("" + String.format("%3d", d.getPatients().size()) + " - " + );
		});
		specializationNum.keySet().forEach( k -> {
			specializationStrings.add("" + String.format("%3d", specializationNum.get(k)) + " - " + k);
		});
		
		return specializationStrings;
	}

	public void loadData(String path) throws IOException {
		try(BufferedReader buffer = new BufferedReader(new FileReader("in.data"))){
			String line;
			while ((line = buffer.readLine()) != null){
				String[] splittedStrings = line.split(";");
				try{
					if(splittedStrings[0] == "P"){ //if patient
						String first = splittedStrings[1];
						String last = splittedStrings[2];
						int ssn;
						try{
							ssn = Integer.parseInt(splittedStrings[3]);
						}catch(NumberFormatException e){ 
							continue;
						}
						//TODO :: tutto corretto puoi caricare in memoria
					}
					if(splittedStrings[0] == "M"){ //if doctor
						int docID;
						try{
							docID = Integer.parseInt(splittedStrings[1]);
						}catch(NumberFormatException e){}
						String first = splittedStrings[2];
						String last = splittedStrings[3];
						String ssn = splittedStrings[4];
						String specialization = splittedStrings[5];
						//TODO : tutto corretto puoi caricare in memoria
					}
				}catch(NullPointerException e){}
			}
		}catch (IOException e) {
			// TODO: handle exception
		}

	}
}



