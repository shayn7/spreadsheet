package dev.naamad.spreadsheet.repository;

import dev.naamad.spreadsheet.document.Spreadsheet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpreadsheetRepository extends MongoRepository<Spreadsheet, String> {
}
