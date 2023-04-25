package com.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pms.entity.PatientInfo;


public interface PatientRepo extends JpaRepository<PatientInfo, String> {

	@Query(value = "select email from patient where email=?1", nativeQuery = true)
	public String existsByEmailId(String email);
	@Query(value = "select password from patient where email=?1", nativeQuery = true)
	public String existsByPassword(String password);
	
	@Query(value = "select * from patient where email=?1", nativeQuery = true)
	public PatientInfo getPatientInfo(String email);
}
