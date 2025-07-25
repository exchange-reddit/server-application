package com.omniversity.public_community_service.Section.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EVENT_SECTION")
public class EventSection extends AbstractSection {

}
