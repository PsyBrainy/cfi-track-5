package com.alkywallet.repository;

import com.alkywallet.dto.ExpenseReportDto;
import com.alkywallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaction, Long> {

    // 1. Historial de transacciones por cuenta, ordenado por fecha desc
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findHistoryByAccountId(@Param("accountId") Long accountId);

    // 2. Reporte de gastos agrupados por tipo con JOIN, GROUP BY y SUM
    @Query("SELECT new com.alkywallet.dto.ExpenseReportDto(t.type, SUM(t.amount)) " +
           "FROM Transaction t JOIN t.account a " +
           "WHERE a.id = :accountId " +
           "GROUP BY t.type " +
           "HAVING SUM(t.amount) > 0")
    List<ExpenseReportDto> findExpenseReportByAccountId(@Param("accountId") Long accountId);
}
