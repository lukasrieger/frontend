package viewmodel

import model.ContactPartner
import tornadofx.ItemViewModel


class ContactViewModel : ItemViewModel<ContactPartner>() {
    val surname = bind(ContactPartner::surname)
    val lastname = bind(ContactPartner::lastName)
    val phoneNumber = bind(ContactPartner::phoneNumber)
    val url = bind(ContactPartner::url)

    override fun onCommit() {
        item = ContactPartner(
            surname = surname.value,
            lastName = lastname.value,
            phoneNumber = phoneNumber.value,
            url = url.value
        )
    }
}
