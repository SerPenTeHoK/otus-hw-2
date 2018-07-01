package ru.sergey_gusarov.hw2.service.testing.results;

import org.springframework.stereotype.Service;
import ru.sergey_gusarov.hw2.domain.results.IntervieweeResultBase;
import ru.sergey_gusarov.hw2.exception.BizLogicException;
import ru.sergey_gusarov.hw2.util.ResultCheckHelper;

@Service
public class ShowResultsServiceImplShell implements ShowResutlsService {

    @Override
    public void showTestingResult(IntervieweeResultBase intervieweeResult) throws BizLogicException {
        if (intervieweeResult == null) {
            throw new BizLogicException("Не указан пользователь или по нему нет данных");
        }
        boolean isTestPass;
        Integer sum;
        try {
            isTestPass = (new ResultCheckHelper()).isTestPass(intervieweeResult.getQuestions());
            sum = (new ResultCheckHelper()).getSumScore(intervieweeResult.getQuestions());
        } catch (BizLogicException ex) {
            throw ex;
        }

        StringBuilder sb = new StringBuilder(140);
        sb.append("Результаты:\n");
        sb.append("Пользователь: ").append(intervieweeResult.getPerson().getFullName());
        if (isTestPass)
            sb.append("\nТест пройден! Поздравлем!\n");
        else
            sb.append("\nТест вами не пройден. Поробуйте в следующий раз.\n");
        sb.append("\nНабранные баллы: ").append(sum);
        System.out.println(sb.toString());
    }
}
