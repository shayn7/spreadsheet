package dev.naamad.spreadsheet.repository;

import dev.naamad.spreadsheet.document.Schema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemaRepository extends MongoRepository<Schema, String> {
}
