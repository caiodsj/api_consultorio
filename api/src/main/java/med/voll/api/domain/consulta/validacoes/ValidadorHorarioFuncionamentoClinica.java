package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta{

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY); //retornar um boolean dizendo q é true domingo
        var antesDaAberturaClinica = dataConsulta.getHour() < 7;
        var depoisDoEncerramentoClinica = dataConsulta.getHour() > 18;

        if(domingo || antesDaAberturaClinica || depoisDoEncerramentoClinica){
            throw new ValidacaoException("Consulta fora do horário de funcionamento!");
        }
    }
}
