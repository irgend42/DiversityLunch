package de.brockhausag.diversitylunchspringboot.dimensions.repositories;

import de.brockhausag.diversitylunchspringboot.dimensions.entities.model.MultiselectDimensionSelectableOption;

import org.springframework.stereotype.Repository;

@Repository
public interface MultiselectDimensionSelectableOptionRepository extends SelectableOptionsRepository<MultiselectDimensionSelectableOption> {
}