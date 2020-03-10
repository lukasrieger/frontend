package viewmodel

import model.ContactPartner
import tornadofx.ItemViewModel


class ContactViewModel : ItemViewModel<ContactPartner>() {
    val surname = bind(ContactPartner::surname)
    val lastname = bind(ContactPartner::lastname)
    val phoneNumber = bind(ContactPartner::phoneNumber)
    val url = bind(ContactPartner::url)

    override fun onCommit() {
        item = ContactPartner(
            surname = surname.value,
            lastname = lastname.value,
            phoneNumber = phoneNumber.value,
            url = url.value
        )
    }
}
