package re.edu.quan_ly_thuc_tap.service;

import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipPhaseResponse;

import java.util.List;

public interface IInternshipPhaseService {
    List<InternshipPhaseResponse> getAllPhases();
    InternshipPhaseResponse getPhaseById(Long phaseId);
    InternshipPhaseResponse createPhase(InternshipPhaseCreateRequestDTO dto);
    InternshipPhaseResponse updatePhase(Long phaseId, InternshipPhaseUpdateRequestDTO dto);
    void deletePhase(Long phaseId);
}