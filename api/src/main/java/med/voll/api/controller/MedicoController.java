package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;
    @PostMapping
    @Transactional //spring - transação pra carregar no banco de dados
    public ResponseEntity cadastrar(@RequestBody @Valid MedicoDTO dados, UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        repository.save(medico); //construtor pra receber dados
        var uri =  uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping //LIST - .toList() se quiser retornar toda a lista. se quiser controlar por pagina usa dessa forma
    public ResponseEntity<Page<GetMedicoDTO>> getAllMedicos(Pageable paginacao){ //importa do pacote spring.domain
        var page = repository.findAllByAtivoTrue(paginacao).map(GetMedicoDTO::new);
        return ResponseEntity.ok(page);
    }

//POR PADRAO O pageble vem 20 por pagina. ----- @PeaglebleDefault(size=10, page = x, sort = {"nome"}) Pagable paginacao ----- <<MUDAR O PADRAO DO PAGE
//http://localhost:8080/medicos?size=10&page=1 [?size=xx&page=x] CONTROLAR QUANTOS ITENS POR PAGINA E QUAL PAGINA QUER MOSTRAR. pagina começa com 0
// NO FRONT VAI FAZER ESSA CONSULTA
//http://localhost:8080/medicos?sort=nome [NOME = nome do atributo do medico] ORDENAR PELO NOME

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid MedicoUpdateDTO dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarMedico(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    //EXCLUSÃO LOGICA - APENAS DESATIVAR O MEDICO PRA N TER PROBLEMA COM OUTRAS TABELAS Q ELE PODE ESTAR RELACIONADO. NAO MOSTRAR NA LISTAGEM
    @DeleteMapping("/{id}") //{} parametro dinamico
    @Transactional
    public ResponseEntity deleteMedico(@PathVariable Long id){
        //repository.deleteById(id); EXCLUSÃO FÍSICA
        var medico = repository.getReferenceById(id);
        medico.inativar();
        return ResponseEntity.noContent().build();
    }

    //PEGAR PELO id
    @GetMapping("/{id}")
    public ResponseEntity getMedicoById(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
