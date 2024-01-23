package dev.naamad.spreadsheet.repository;

import dev.naamad.spreadsheet.document.Sheet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends MongoRepository<Sheet, String> {
}
